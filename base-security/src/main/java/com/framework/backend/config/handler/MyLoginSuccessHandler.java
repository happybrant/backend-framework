package com.framework.backend.config.handler;

import com.alibaba.fastjson2.JSON;
import com.framework.backend.common.ResponseData;
import com.framework.backend.enums.ResultCode;
import com.framework.backend.model.entity.User;
import com.framework.backend.utils.RedisCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * @author fucong
 * @since 2023/6/1
 * @description 登录成功处理器 序列化处理
 */
@Component("myLoginSuccessHandler")
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {
  @Autowired private RedisCache redisCache;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      Authentication authentication)
      throws IOException {
    // 设置响应的格式
    httpServletResponse.setContentType("application/json;charset=utf-8");
    ResponseData<User> result = new ResponseData<>(ResultCode.LOGIN_SUCCESS);
    User user = (User) authentication.getPrincipal();
    // 登录成功处理
    result.setData(user);
    PrintWriter writer = httpServletResponse.getWriter();
    writer.write(JSON.toJSONString(result));
    writer.flush();
  }
}
