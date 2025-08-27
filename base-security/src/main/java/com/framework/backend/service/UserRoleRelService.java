package com.framework.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.model.entity.UserRoleRel;
import java.util.List;

/**
 * @author fucong
 * @description 授权服务接口
 * @since 2025/6/26 13:47
 */
public interface UserRoleRelService extends IService<UserRoleRel> {

  /**
   * 绑定用户和角色
   *
   * @param userRoleRelList
   */
  void bindUserRole(List<UserRoleRel> userRoleRelList);
}
