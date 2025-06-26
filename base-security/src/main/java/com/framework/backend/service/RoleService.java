package com.framework.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.entity.Role;

import java.util.List;

/**
 * @author fucong
 * @description To do
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
}
