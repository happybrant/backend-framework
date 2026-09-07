package com.framework.backend.config.handler;

import com.alibaba.fastjson2.JSON;
import com.framework.backend.common.ResponseData;
import com.framework.backend.enums.ResultCode;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author fucong
 * @since 2025/07/22 17:07
 * @description 匿名用户访问资源处理器
 */
@Component("myAuthenticationHandler")
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Resource
  private RequestMappingHandlerMapping requestMappingHandlerMapping;

  @Override
  public void commence(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException e)
      throws IOException {
    // 判断请求的 URL 是否对应实际存在的 Controller 接口
    if (!isValidRoute(httpServletRequest)) {
      httpServletResponse.setContentType("application/json;charset=utf-8");
      httpServletResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
      ResponseData<Object> result = new ResponseData<>(ResultCode.NOT_FOUND);
      PrintWriter out = httpServletResponse.getWriter();
      out.write(JSON.toJSONString(result));
      out.flush();
      out.close();
      return;
    }

    // 路由存在但用户未认证，返回 401 未授权错误信息
    httpServletResponse.setContentType("application/json;charset=utf-8");
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    ResponseData<Object> result = new ResponseData<>(ResultCode.UNAUTHORIZED);
    PrintWriter out = httpServletResponse.getWriter();
    out.write(JSON.toJSONString(result));
    out.flush();
    out.close();
  }

  /**
   * 判断当前请求是否对应一个已注册的 Controller 方法
   */
  private boolean isValidRoute(HttpServletRequest request) {
    try {
      HandlerExecutionChain handler = requestMappingHandlerMapping.getHandler(request);
      return handler != null;
    } catch (Exception ex) {
      return false;
    }
  }
}