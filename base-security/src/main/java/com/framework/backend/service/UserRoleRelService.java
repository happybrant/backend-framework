package com.framework.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.model.dto.RoleUserDto;
import com.framework.backend.model.dto.UserRoleDto;
import com.framework.backend.model.entity.UserRoleRel;

/**
 * @author fucong
 * @description 授权服务接口
 * @since 2025/6/26 13:47
 */
public interface UserRoleRelService extends IService<UserRoleRel> {

  /**
   * 绑定用户的角色
   *
   * @param userRoleDto
   */
  void bindUserRole(UserRoleDto userRoleDto);

  /**
   * 绑定角色下的用户
   *
   * @param roleUserDto
   */
  void bindRoleUser(RoleUserDto roleUserDto);
}
