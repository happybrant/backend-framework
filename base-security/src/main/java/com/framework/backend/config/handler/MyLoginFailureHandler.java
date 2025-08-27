package com.framework.backend.config.handler;

import com.alibaba.fastjson2.JSON;
import com.framework.backend.common.ResponseData;
import com.framework.backend.enums.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * @author fucong
 * @since 2023/6/1
 * @description 登录失败处理器 序列化处理
 */
@Component("myLoginFailureHandler")
public class MyLoginFailureHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException exception)
      throws IOException {
    // 处理失败的后置操作
    httpServletResponse.setContentType("application/json;charset=utf-8");
    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

    String message = getMessage(exception);
    // 设置返回格式
    ResponseData<String> result = new ResponseData<>(ResultCode.LOGIN_FAIL.getCode(), message);
    PrintWriter writer = httpServletResponse.getWriter();
    writer.write(JSON.toJSONString(result));
    writer.flush();
  }

  private static String getMessage(AuthenticationException exception) {
    String str;
    if (exception instanceof AccountExpiredException) {
      str = "账户过期，登录失败!";
    } else if (exception instanceof BadCredentialsException) {
      str = "用户名或密码错误，登录失败!";
    } else if (exception instanceof CredentialsExpiredException) {
      str = "密码过期，登录失败!";
    } else if (exception instanceof DisabledException) {
      str = "账户被禁用，登录失败!";
    } else if (exception instanceof LockedException) {
      str = "账户被锁，登录失败!";
    } else if (exception instanceof InternalAuthenticationServiceException) {
      str = "账户不存在，登录失败!";
    } else {
      str = "登录失败!";
    }
    return str;
  }
}
