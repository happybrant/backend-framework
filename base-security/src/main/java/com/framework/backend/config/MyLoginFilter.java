package com.framework.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 使用自定义登录过滤器，登录时可以额外支持json数据格式参数
 *
 * @author xiafeng
 */
public class MyLoginFilter extends UsernamePasswordAuthenticationFilter {
  @SneakyThrows
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    if (!"POST".equals(request.getMethod())) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }
    // 登录时支持json数据格式
    String contentType = request.getContentType();
    if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(contentType)) {
      Map<String, String> userInfo;
      userInfo = new ObjectMapper().readValue(request.getInputStream(), Map.class);
      String username = userInfo.get(getUsernameParameter());
      username = (username != null) ? username : "";
      username = username.trim();
      String password = userInfo.get(getPasswordParameter());
      password = (password != null) ? password : "";
      UsernamePasswordAuthenticationToken authRequest =
          new UsernamePasswordAuthenticationToken(username, password);
      setDetails(request, authRequest);
      return this.getAuthenticationManager().authenticate(authRequest);
    }
    // 登录时使用原有的form数据格式参数
    return super.attemptAuthentication(request, response);
  }
}
