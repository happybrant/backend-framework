package com.framework.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.common.MyPage;
import com.framework.backend.model.entity.SecurityLog;

/**
 * @author fucong
 * @description 角色服务接口
 * @since 2025/6/26 13:47
 */
public interface LogService extends IService<SecurityLog> {

  /**
   * 分页查找日志
   *
   * @param log
   */
  MyPage<SecurityLog> pageList(SecurityLog log);
}
