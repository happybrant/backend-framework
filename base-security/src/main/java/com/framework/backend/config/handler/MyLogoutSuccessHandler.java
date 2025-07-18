package com.framework.backend.config.handler;

import com.alibaba.fastjson2.JSON;
import com.framework.backend.common.ResponseData;
import com.framework.backend.enums.ResultCode;
import com.framework.backend.utils.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author fucong
 * @since 2023/11/30 17:07
 * @description 成功退出处理器
 */
@Component("logoutSuccessHandler")
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
  @Autowired private RedisCache redisCache;

  @Override
  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    // 设置响应的格式
    response.setContentType("application/json;charset=utf-8");
    ResponseData<String> result = new ResponseData<>(ResultCode.LOGOUT_SUCCESS);
    PrintWriter writer = response.getWriter();
    writer.write(JSON.toJSONString(result));
    writer.flush();
  }
}
