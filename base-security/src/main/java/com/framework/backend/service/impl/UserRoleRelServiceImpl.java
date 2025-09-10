package com.framework.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.mapper.UserRoleMapper;
import com.framework.backend.model.dto.RoleUserDto;
import com.framework.backend.model.dto.UserRoleDto;
import com.framework.backend.model.entity.UserRoleRel;
import com.framework.backend.service.UserRoleRelService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description 角色服务实现类
 * @since 2025/6/26 13:48
 */
@Service
public class UserRoleRelServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleRel>
    implements UserRoleRelService {

  @Override
  public void bindUserRole(UserRoleDto userRoleDto) {
    // 移除该用户下的所有角色
    QueryWrapper<UserRoleRel> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(UserRoleRel::getUserId, userRoleDto.getUserId());
    remove(queryWrapper);
    // 添加新角色
    if (userRoleDto.getRoleIds() != null && !userRoleDto.getRoleIds().isEmpty()) {
      List<UserRoleRel> userRoleRelList = new ArrayList<>();
      for (String roleId : userRoleDto.getRoleIds()) {
        UserRoleRel userRoleRel = new UserRoleRel();
        userRoleRel.setRoleId(roleId);
        userRoleRel.setUserId(userRoleDto.getUserId());
        userRoleRelList.add(userRoleRel);
      }
      saveBatch(userRoleRelList);
    }
  }

  @Override
  public void bindRoleUser(RoleUserDto roleUserDto) {
    // 移除该角色下的所有用户
    QueryWrapper<UserRoleRel> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(UserRoleRel::getRoleId, roleUserDto.getRoleId());
    remove(queryWrapper);
    // 添加新角色
    if (roleUserDto.getUserIds() != null && !roleUserDto.getUserIds().isEmpty()) {
      List<UserRoleRel> userRoleRelList = new ArrayList<>();
      for (String userId : roleUserDto.getUserIds()) {
        UserRoleRel userRoleRel = new UserRoleRel();
        userRoleRel.setUserId(userId);
        userRoleRel.setRoleId(roleUserDto.getRoleId());
        userRoleRelList.add(userRoleRel);
      }
      saveBatch(userRoleRelList);
    }
  }
}
