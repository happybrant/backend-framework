package com.framework.backend.config.handler;

import com.alibaba.fastjson2.JSON;
import com.framework.backend.common.ResponseData;
import com.framework.backend.enums.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * @author fucong
 * @since 2023/11/30 17:07
 * @description 权限不够处理器
 */
@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      AccessDeniedException e)
      throws IOException {
    // 返回http 403 Forbidden错误信息
    httpServletResponse.setContentType("application/json;charset=utf-8");
    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);

    ResponseData<String> result = new ResponseData<>(ResultCode.FORBIDDEN);
    PrintWriter writer = httpServletResponse.getWriter();
    writer.write(JSON.toJSONString(result));
    writer.flush();
  }
}
