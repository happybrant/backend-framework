package com.framework.backend.aspectj;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.backend.annotation.Log;
import com.framework.backend.model.entity.SecurityLog;
import com.framework.backend.model.entity.User;
import com.framework.backend.service.LogService;
import com.framework.backend.utils.IpUtils;
import com.framework.backend.utils.SecurityUtils;
import com.framework.backend.utils.SpringBeanUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author ZC
 */
@Aspect
@Component
public class SecurityLogAspect {
  @Autowired ObjectMapper jsonMapper;
  @Autowired LogService logService;

  private static final ThreadLocal<LocalDateTime> TIME_THREAD_LOCAL = new ThreadLocal<>();

  /** 参数名发现器 */
  private DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

  /** spel 表达式解析器 */
  private ExpressionParser parser = new SpelExpressionParser();

  /** 定义一个切入点 */
  @Pointcut("@annotation(com.framework.backend.annotation.Log)")
  public void operationLog() {}

  /** 异常通知 */
  @AfterThrowing(value = "operationLog()", throwing = "e")
  public void afterThrowing(JoinPoint jp, Exception e) {
    SecurityLog log = new SecurityLog();
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      // 获取客户端的IP地址
      String requestIp = IpUtils.getIpAddress(request);
      log.setRequestIp(requestIp);
      // 获取请求客户端信息
      log.setAgent(request.getHeader("User-Agent"));
    }
    MethodSignature methodSignature = (MethodSignature) jp.getSignature();
    // 获取方法上的注解配置
    Log logAnnotation = methodSignature.getMethod().getAnnotation(Log.class);
    // 解析占位符获取spel字符串配置。如无配置，返回原字段。
    String expression =
        SpringBeanUtils.getApplicationContext()
            .getEnvironment()
            .resolvePlaceholders(logAnnotation.value());
    // 解析spel获取实际内容
    String content =
        parseExpression(methodSignature.getMethod(), (ProceedingJoinPoint) jp, expression);
    log.setContent(content);
    log.setModule(logAnnotation.module());
    LocalDateTime startTime = TIME_THREAD_LOCAL.get();
    LocalDateTime endTime = LocalDateTime.now();
    log.setStartTime(startTime);
    log.setEndTime(endTime);
    ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
    log.setCost(
        endTime.toInstant(zoneOffset).toEpochMilli()
            - startTime.toInstant(zoneOffset).toEpochMilli());
    // 保存用户信息
    User user = SecurityUtils.getCurrentUser();
    if (user != null) {
      log.setAccount(user.getUsername());
    }
    log.setResultFlag("failure");
    log.setErrorMsg(e.getMessage());
    logService.save(log);
  }

  /** 环绕通知 */
  @Around("operationLog()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    SecurityLog log = new SecurityLog();
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      // 获取客户端的IP地址
      String requestIp = IpUtils.getIpAddress(request);
      log.setRequestIp(requestIp);
      // 获取请求客户端信息
      log.setAgent(request.getHeader("User-Agent"));
    }
    // 统计方法执行时间
    LocalDateTime startTime = LocalDateTime.now();
    log.setStartTime(startTime);
    TIME_THREAD_LOCAL.set(startTime);
    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    // 获取方法上的注解配置
    Log logAnnotation = methodSignature.getMethod().getAnnotation(Log.class);
    // 解析占位符获取spel字符串配置。如无配置，返回原字段。
    String spel =
        SpringBeanUtils.getApplicationContext()
            .getEnvironment()
            .resolvePlaceholders(logAnnotation.value());
    // 解析spel获取实际内容
    String content = parseExpression(methodSignature.getMethod(), pjp, spel);
    log.setContent(content);
    String module = logAnnotation.module();
    log.setModule(module);
    // 执行方法
    Object result = pjp.proceed();
    LocalDateTime endTime = LocalDateTime.now();
    log.setEndTime(endTime);
    ZoneOffset zoneOffset = ZoneOffset.ofHours(8);
    log.setCost(
        endTime.toInstant(zoneOffset).toEpochMilli()
            - startTime.toInstant(zoneOffset).toEpochMilli());
    // 保存用户信息
    User user = SecurityUtils.getCurrentUser();
    if (user != null) {
      log.setAccount(user.getUsername());
    }
    log.setResultFlag("success");
    logService.save(log);

    return result;
  }

  /**
   * 解析 spel 表达式
   *
   * @param method 方法
   * @param pjp
   * @param spel 表达式
   * @return 执行spel表达式后的结果
   */
  private String parseExpression(Method method, ProceedingJoinPoint pjp, String spel) {
    // 获取方法的形参名称
    String[] params = discoverer.getParameterNames(method);
    if (params == null) {
      return "";
    }
    // 获取方法的实际参数值
    Object[] arguments = pjp.getArgs();
    Map<String, Object> variables = new ConcurrentHashMap<>();
    for (int i = 0; i < params.length; i++) {
      // 处理当controller方法参数未传值时为null，而ConcurrentHashMap put时会报空指针的问题
      if (arguments[i] != null) {
        variables.put(params[i], arguments[i]);
      }
    }
    // 设置解析spel所需的数据上下文
    StandardEvaluationContext context = new StandardEvaluationContext();
    context.addPropertyAccessor(new MapAccessor());
    context.setRootObject(variables);
    // 解析表达式并获取spel的值
    Expression expression = parser.parseExpression(spel, new TemplateParserContext());
    return (String) expression.getValue(context);
  }
}
