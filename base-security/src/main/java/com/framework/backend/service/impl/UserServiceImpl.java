package com.framework.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.common.MyBaseEntity;
import com.framework.backend.common.MyPage;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.mapper.UserMapper;
import com.framework.backend.model.entity.User;
import com.framework.backend.service.AuthorizationService;
import com.framework.backend.service.RoleService;
import com.framework.backend.service.UserService;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description 用户服务实现类
 * @since 2025/6/26 13:48
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
  @Value("${base.security.default.pwd}")
  private String defaultPwd;

  @Value("${base.security.login.password.regexp}")
  private String passwordRegex;

  @Autowired private RoleService roleService;
  @Autowired private AuthorizationService authorizationService;

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
    List<String> permissions = authorizationService.getByUserId(user.getId());
    user.setPermissions(permissions);
    return user;
  }

  @Override
  public void addUser(User user) {
    if (StringUtils.isBlank(user.getUsername())) {
      throw new BusinessException("账号不能为空！");
    }
    if (StringUtils.isEmpty(user.getPassword())) {
      user.setPassword(defaultPwd);
    } else {
      Pattern pattern = Pattern.compile(passwordRegex);
      Matcher matcher = pattern.matcher(user.getPassword());
      if (!matcher.find()) {
        throw new BusinessException("密码不符合规则！");
      }
    }
    // 查看用户名有没有重复
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(User::getUsername, user.getUsername());
    List<User> list = list(queryWrapper);
    if (!list.isEmpty()) {
      throw new BusinessException("当前用户名已存在！");
    }
    // 密码加密
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encodePwd = encoder.encode(user.getPassword());
    user.setPassword(encodePwd);
    save(user);
  }

  @Override
  public void updateUser(User user) {
    if (StringUtils.isNotBlank(user.getId())) {
      throw new BusinessException("用户id不能为空");
    }
    // 查看用户名有没有重复
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .lambda()
        .eq(User::getUsername, user.getUsername())
        .ne(MyBaseEntity::getId, user.getId());
    List<User> list = list(queryWrapper);
    if (!list.isEmpty()) {
      throw new BusinessException("当前用户名已存在！");
    }
    updateById(user);
  }

  @Override
  public void updatePwd(User user) {
    if (StringUtils.isNotBlank(user.getId())) {
      throw new BusinessException("用户id不能为空！");
    }
    User exist = getById(user.getId());
    String oldPassword = user.getOldPassword();
    String newPassword = user.getNewPassword();
    if (StringUtils.isNotBlank(oldPassword)) {
      throw new BusinessException("原密码不能为空！");
    }
    if (StringUtils.isNotBlank(newPassword)) {
      throw new BusinessException("新密码不能为空！");
    }
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    String encodePwd = encoder.encode(oldPassword);
    if (!exist.getPassword().equals(encodePwd)) {
      throw new BusinessException("旧密码输入错误！");
    }
    Pattern pattern = Pattern.compile(passwordRegex);
    Matcher matcher = pattern.matcher(newPassword);
    if (!matcher.find()) {
      throw new BusinessException("新密码不符合规则！");
    }
    if (newPassword.equals(oldPassword)) {
      throw new BusinessException("新密码不能和旧密码相同！");
    }
    User updateUser = new User();
    updateUser.setId(user.getId());
    updateUser.setPassword(encoder.encode(newPassword));
    baseMapper.updateById(updateUser);
  }

  @Override
  public void resetPwd(User user) {
    if (StringUtils.isNotBlank(user.getId())) {
      throw new BusinessException("用户id不能为空！");
    }
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    User updateUser = new User();
    updateUser.setId(user.getId());
    updateUser.setPassword(encoder.encode(defaultPwd));
    updateById(updateUser);
  }

  @Override
  public MyPage<User> pageList(User user) {
    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
    if (StringUtils.isNotBlank(user.getUsername())) {
      queryWrapper.lambda().like(User::getUsername, user.getUsername());
    }
    if (StringUtils.isNotBlank(user.getRealName())) {
      queryWrapper.lambda().like(User::getRealName, user.getRealName());
    }
    if (StringUtils.isNotBlank(user.getStatus())) {
      queryWrapper.lambda().eq(User::getStatus, user.getStatus());
    }
    MyPage<User> page = new MyPage<>(user.getCurrentPage(), user.getPageSize());
    page = baseMapper.selectPage(page, queryWrapper);
    return page;
  }
}
