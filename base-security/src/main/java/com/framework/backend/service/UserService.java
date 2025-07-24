package com.framework.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.model.entity.User;

/**
 * @author fucong
 * @description 用户服务接口
 * @since 2025/6/26 13:47
 */
public interface UserService extends IService<User> {
  /**
   * 根据用户名查询用户
   *
   * @param name
   * @return
   */
  User getByUsername(String name);
}
