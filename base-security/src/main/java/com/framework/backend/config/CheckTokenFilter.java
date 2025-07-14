package com.framework.backend.config;

import com.framework.backend.config.exception.CustomerAuthenticationException;
import com.framework.backend.config.handler.MyLoginFailureHandler;
import com.framework.backend.utils.RedisCache;
import com.framework.backend.utils.TokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Data
@Component("checkTokenFilter")
@EqualsAndHashCode(callSuper = false)
public class CheckTokenFilter extends OncePerRequestFilter {
  @Value("${base.security.login.url}")
  private String loginUrl;

  @Autowired private MyLoginFailureHandler loginFailureHandler;
  @Autowired private MyUserDetailsService myUserDetailsService;

  @Resource private RedisCache redisCache;

  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {
    // 获取请求的url(读取配置文件的url)
    String url = httpServletRequest.getRequestURI();
    if (StringUtils.contains(httpServletRequest.getServletPath(), "swagger")
        || StringUtils.contains(httpServletRequest.getServletPath(), "webjars")
        || StringUtils.contains(httpServletRequest.getServletPath(), "v3")
        || StringUtils.contains(httpServletRequest.getServletPath(), "profile")
        || StringUtils.contains(httpServletRequest.getServletPath(), "swagger-ui")
        || StringUtils.contains(httpServletRequest.getServletPath(), "swagger-resources")
        || StringUtils.contains(httpServletRequest.getServletPath(), "csrf")
        || StringUtils.contains(httpServletRequest.getServletPath(), "favicon")
        || StringUtils.contains(httpServletRequest.getServletPath(), "v2")) {
      filterChain.doFilter(httpServletRequest, httpServletResponse);
    } else if (StringUtils.equals(url, loginUrl)) {
      // 是登录请求放行
      filterChain.doFilter(httpServletRequest, httpServletResponse);
    } else {
      try {
        // token验证（如果不是登录请求 验证token）
        if (!url.equals(loginUrl)) {
          validateToken(httpServletRequest);
        }
      } catch (AuthenticationException e) {
        loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
        return;
      }
      filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
  }

  // token验证
  private void validateToken(HttpServletRequest request) {
    // 从请求的头部获取token
    String token = request.getHeader("token");
    // 如果请求头部没有获取到token，则从请求参数中获取token
    if (StringUtils.isEmpty(token)) {
      token = request.getParameter("token");
    }
    if (StringUtils.isEmpty(token)) {
      // 请求参数中也没有 那就从redis中进行获取根据ip地址取
      token = redisCache.getCacheObject(request.getRemoteAddr());
    }
    if (StringUtils.isEmpty(token)) {
      throw new CustomerAuthenticationException("token不存在！");
    }
    // 解析token
    String username = TokenUtil.getUserFromToken(token);
    if (StringUtils.isEmpty(username)) {
      throw new CustomerAuthenticationException("token解析失败!");
    }
    // 获取用户信息
    UserDetails user = myUserDetailsService.loadUserByUsername(username);
    if (user == null) {
      throw new CustomerAuthenticationException("token验证失败!");
    }
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    // 设置到spring security上下文
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }
}
