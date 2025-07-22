package com.framework.backend.config.handler;

import com.alibaba.fastjson.JSON;
import com.framework.backend.common.ResponseData;
import com.framework.backend.enums.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @author fucong
 * @since 2025/07/22 17:07
 * @description 匿名用户访问资源处理器
 */
@Component("myAuthenticationHandler")
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
  public void commence(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AuthenticationException e)
      throws IOException {
    httpServletResponse.setContentType("application/json;charset=utf-8");
    httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    ResponseData<Object> result = new ResponseData<>(ResultCode.UNAUTHORIZED);
    PrintWriter out = httpServletResponse.getWriter();
    out.write(JSON.toJSONString(result));
    out.flush();
    out.close();
  }
}
