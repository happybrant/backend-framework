package com.framework.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.model.entity.Authorization;
import java.util.List;

/**
 * @author fucong
 * @description 授权服务接口
 * @since 2025/6/26 13:47
 */
public interface AuthorizationService extends IService<Authorization> {

  /**
   * 根据用户id获取资源码
   *
   * @param userId
   * @return
   */
  List<String> getByUserId(String userId);
}
