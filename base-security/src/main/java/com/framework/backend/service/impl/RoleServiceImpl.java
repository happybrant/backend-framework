package com.framework.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.entity.Role;
import com.framework.backend.entity.UserRoleRel;
import com.framework.backend.mapper.RoleMapper;
import com.framework.backend.mapper.UserRoleMapper;
import com.framework.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author fucong
 * @description To do
 * @since 2025/6/26 13:48
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

  @Autowired private UserRoleMapper userRoleMapper;

  @Override
  public List<String> getByUserId(String id) {
    List<String> roleCodes = new ArrayList<>();
    QueryWrapper<UserRoleRel> queryWrapper = new QueryWrapper<>();
    List<UserRoleRel> userRoleRelList = userRoleMapper.selectList(queryWrapper);
    if (userRoleRelList != null) {
      List<String> roleIds =
          userRoleRelList.stream().map(UserRoleRel::getRoleId).collect(Collectors.toList());
      List<Role> roles = listByIds(roleIds);
      if (roles != null) {
        roleCodes = roles.stream().map(Role::getCode).collect(Collectors.toList());
      }
    }
    return roleCodes;
  }
}
