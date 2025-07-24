package com.framework.backend.config.handler;

import com.alibaba.fastjson2.JSON;
import com.framework.backend.common.AuthConstant;
import com.framework.backend.common.ResponseData;
import com.framework.backend.enums.ResultCode;
import com.framework.backend.model.entity.User;
import com.framework.backend.model.vo.LoginResultObject;
import com.framework.backend.utils.RedisCache;
import com.framework.backend.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;
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
    ResponseData<LoginResultObject> result = new ResponseData<>(ResultCode.LOGIN_SUCCESS);
    User user = (User) authentication.getPrincipal();
    // 登录成功处理
    // 1.生成token
    String token = TokenUtil.genAccessToken(user.getUsername());
    long expireTime = TokenUtil.getExpirationTime(token);
    String uuid = TokenUtil.getIdFromToken(token);
    // 配置一下返回给前端的token信息
    LoginResultObject vo = new LoginResultObject();
    // 将用户信息返回给前端
    vo.setUserInfo(user);
    // 将用户存入redis中
    redisCache.setCacheObject(
        AuthConstant.buildLoginKey(uuid), user, TokenUtil.ACCESS_EXPIRE, TimeUnit.SECONDS);
    vo.setToken(token);
    vo.setExpireTime(expireTime);
    result.setData(vo);
    PrintWriter writer = httpServletResponse.getWriter();
    writer.write(JSON.toJSONString(result));
    writer.flush();
  }
}
