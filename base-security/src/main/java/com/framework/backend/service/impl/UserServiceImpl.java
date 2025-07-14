package com.framework.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.entity.User;
import com.framework.backend.mapper.UserMapper;
import com.framework.backend.service.RoleService;
import com.framework.backend.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description To do
 * @since 2025/6/26 13:48
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
  @Autowired RoleService roleService;

  @Override
  public User getByUsername(String name) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(User::getUsername, name).last("limit 1");
    User user = getOne(queryWrapper);
    if (user == null) {
      throw new UsernameNotFoundException("用户不存在！");
    }
    List<String> roleCodes = roleService.getByUserId(user.getId());
    user.setRoleCodes(roleCodes);
    return user;
  }
}
