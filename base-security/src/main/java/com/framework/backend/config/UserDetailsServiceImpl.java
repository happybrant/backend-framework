package com.framework.backend.config;

import com.framework.backend.entity.User;
import com.framework.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author fucong
 * @description To do
 * @since 2025/6/26 13:45
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.getByUsername(username);
    if (user == null) {
      return null;
    }
    if (!user.getRoleCodes().isEmpty()) {
      Collection<GrantedAuthority> grantedAuthorities =
          user.getRoleCodes().stream()
              .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
              .collect(Collectors.toList());
      user.setAuthorities(grantedAuthorities);
    }
    return user;
  }
}
