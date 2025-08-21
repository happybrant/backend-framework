package com.framework.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.model.entity.Role;
import java.util.List;

/**
 * @author fucong
 * @description 角色服务接口
 * @since 2025/6/26 13:47
 */
public interface RoleService extends IService<Role> {
  /**
   * 根基用户id获取角色code列表
   *
   * @param userId
   * @return
   */
  List<String> getByUserId(String userId);

  /**
   * 添加角色
   *
   * @param role
   */
  void addRole(Role role);

  /**
   * 修改角色
   *
   * @param role
   */
  void updateRole(Role role);

  /**
   * 根据角色id批量删除角色
   *
   * @param ids
   */
  void removeRoleByIds(List<String> ids);
}
