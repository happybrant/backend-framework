package com.framework.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.common.MyPage;
import com.framework.backend.model.entity.Role;
import com.framework.backend.model.entity.User;
import java.util.List;

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

  /**
   * 添加用户
   *
   * @param user
   */
  void addUser(User user);

  /**
   * 修改用户
   *
   * @param user
   */
  void updateUser(User user);

  /**
   * 修改密码
   *
   * @param user
   */
  void updatePwd(User user);

  /**
   * 重置密码
   *
   * @param user
   */
  void resetPwd(User user);

  /**
   * 分页查找用户
   *
   * @param user
   * @return
   */
  MyPage<User> pageList(User user);

  /**
   * 查看角色下用户数量
   *
   * @param roleIds
   * @return
   */
  int getCountByRoleIds(List<String> roleIds);

  /**
   * 查看角色下的用户列表
   *
   * @param role
   * @return
   */
  List<User> getUserListByRole(Role role);

  /**
   * 查看单条用户信息
   *
   * @param user
   * @return
   */
  User getOne(User user);
}
