package com.framework.backend.utils;

import com.framework.backend.entity.User;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author fucong
 * @description 安全工具类，获取登录用户相关信息
 * @since 2025/7/18 15:43
 */
public class SecurityUtil {

  /**
   * 获取当前登录用户信息
   *
   * @return
   */
  public static User getCurrentUser() {
    return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }

  public static Collection<? extends GrantedAuthority> getUserAuthorities() {
    return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
  }
}
