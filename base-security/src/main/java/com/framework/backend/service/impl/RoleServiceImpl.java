package com.framework.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.common.MyBaseEntity;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.mapper.RoleMapper;
import com.framework.backend.mapper.UserMapper;
import com.framework.backend.mapper.UserRoleMapper;
import com.framework.backend.model.entity.Role;
import com.framework.backend.model.entity.UserRoleRel;
import com.framework.backend.service.RoleService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description 角色服务实现类
 * @since 2025/6/26 13:48
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

  @Autowired private UserRoleMapper userRoleMapper;
  @Autowired private UserMapper userMapper;

  @Override
  public List<String> getByUserId(String id) {
    List<String> roleCodes = new ArrayList<>();
    QueryWrapper<UserRoleRel> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(UserRoleRel::getUserId, id);
    List<UserRoleRel> userRoleRelList = userRoleMapper.selectList(queryWrapper);
    if (!userRoleRelList.isEmpty()) {
      List<String> roleIds =
          userRoleRelList.stream().map(UserRoleRel::getRoleId).collect(Collectors.toList());
      List<Role> roles = listByIds(roleIds);
      if (roles != null) {
        roleCodes = roles.stream().map(Role::getCode).collect(Collectors.toList());
      }
    }
    return roleCodes;
  }

  @Override
  public void addRole(Role role) {
    // 查看角色编码有没有重复
    QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(Role::getCode, role.getCode());
    List<Role> list = list(queryWrapper);
    if (!list.isEmpty()) {
      throw new BusinessException("当前角色编码已存在！");
    }
    role.setStatus("active");
    save(role);
  }

  @Override
  public void updateRole(Role role) {
    if (StringUtils.isNotBlank(role.getId())) {
      throw new BusinessException("用户id不能为空！");
    }
    // 查看角色编码有没有重复
    QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(Role::getCode, role.getCode()).ne(MyBaseEntity::getId, role.getId());
    List<Role> list = list(queryWrapper);
    if (!list.isEmpty()) {
      throw new BusinessException("当前角色编码已存在！");
    }
    updateById(role);
  }

  @Override
  public void removeRoleByIds(List<String> roleIds) {
    int count = userMapper.selectCountByRoleIds(roleIds);
    if (count > 0) {
      throw new BusinessException("当前选中角色下存在用户，无法删除！");
    }
    removeBatchByIds(roleIds);
  }

  @Override
  public void enableRole(Role role) {
    if (StringUtils.isNotBlank(role.getId())) {
      throw new BusinessException("角色id不能为空！");
    }
    role.setStatus("active");
    updateById(role);
  }

  @Override
  public void disableRole(Role role) {
    if (StringUtils.isNotBlank(role.getId())) {
      throw new BusinessException("角色id不能为空！");
    }
    role.setStatus("disabled");
    updateById(role);
  }
}
