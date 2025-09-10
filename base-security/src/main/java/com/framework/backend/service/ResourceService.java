package com.framework.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.framework.backend.model.entity.Resource;
import java.util.List;

/**
 * @author fucong
 * @description 资源服务接口
 * @since 2025/6/26 13:47
 */
public interface ResourceService extends IService<Resource> {

  /**
   * 添加资源
   *
   * @param resource
   */
  void addResource(Resource resource);

  /**
   * 修改资源
   *
   * @param resource
   */
  void updateResource(Resource resource);

  /**
   * 根据资源id批量删除资源
   *
   * @param ids
   */
  void removeResourceByIds(List<String> ids);

  /**
   * 启用资源
   *
   * @param resource
   */
  void enableResource(Resource resource);

  /**
   * 停用资源
   *
   * @param resource
   */
  void disableResource(Resource resource);
}
