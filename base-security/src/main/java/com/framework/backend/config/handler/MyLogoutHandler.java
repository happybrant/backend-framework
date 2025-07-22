package com.framework.backend.config.handler;

import com.framework.backend.common.AuthConstant;
import com.framework.backend.config.exception.CustomerAuthenticationException;
import com.framework.backend.utils.RedisCache;
import com.framework.backend.utils.TokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

/**
 * @author fucong
 * @since 2025/07/20 17:07
 * @description 退出登录处理器
 */
@Component("logoutHandler")
public class MyLogoutHandler implements LogoutHandler {
  @Autowired private RedisCache redisCache;

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    String token = request.getHeader("Authorization");
    // 如果请求头部没有获取到token，则从请求参数中获取token
    if (StringUtils.isEmpty(token)) {
      token = request.getParameter("Authorization");
    }
    if (StringUtils.isEmpty(token)) {
      throw new CustomerAuthenticationException("token不存在！");
    }
    token = token.replace("Bearer ", "");
    String uuid = TokenUtil.getIdFromToken(token);
    // 从redis中删除key
    redisCache.deleteObject(AuthConstant.buildLoginKey(uuid));
  }
}
