package com.framework.backend.config;

import com.framework.backend.model.entity.User;
import com.framework.backend.service.AuthorizationService;
import com.framework.backend.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author fucong
 * @description 自定义UserDetailsService 用于查询用户信息
 * @since 2025/6/26 13:45
 */
@Component
public class MyUserDetailsService implements UserDetailsService {
  @Autowired private UserService userService;
  @Autowired private AuthorizationService authorizationService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.getByUsername(username);
    if (user == null) {
      return null;
    }
    Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    // 1.设置用户的角色权限
    if (!user.getRoleCodes().isEmpty()) {
      grantedAuthorities.addAll(
          user.getRoleCodes().stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
              .toList());
    }
    // 2.设置用户所有的资源权限
    if (!user.getPermissions().isEmpty()) {
      grantedAuthorities.addAll(
          user.getPermissions().stream().map(SimpleGrantedAuthority::new).toList());
    }
    user.setAuthorities(grantedAuthorities);
    return user;
  }
}
