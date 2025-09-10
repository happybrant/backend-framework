package com.framework.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.common.MyBaseEntity;
import com.framework.backend.common.exception.BusinessException;
import com.framework.backend.mapper.ResourceMapper;
import com.framework.backend.model.entity.Resource;
import com.framework.backend.service.ResourceService;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description 角色服务实现类
 * @since 2025/6/26 13:48
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
    implements ResourceService {

  @Override
  public void addResource(Resource resource) {
    // 资源编码不能重复
    QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
    queryWrapper.lambda().eq(Resource::getCode, resource.getCode());
    List<Resource> list = list(queryWrapper);
    if (!list.isEmpty()) {
      throw new BusinessException("当前资源编码已存在！");
    }
    // 同一层级名称不能重复
    String parentId = resource.getParentId();
    String name = resource.getName();
    QueryWrapper<Resource> queryWrapper1 = new QueryWrapper<>();
    queryWrapper1.lambda().eq(Resource::getParentId, parentId);
    queryWrapper1.lambda().eq(Resource::getName, name);
    List<Resource> list1 = list(queryWrapper1);
    if (!list1.isEmpty()) {
      throw new BusinessException("当前层级名称已存在！");
    }
    resource.setStatus("active");
    save(resource);
  }

  @Override
  public void updateResource(Resource resource) {
    if (StringUtils.isNotBlank(resource.getId())) {
      throw new BusinessException("资源id不能为空！");
    }
    Resource exist = getById(resource.getId());
    if (exist == null) {
      throw new BusinessException("找不到资源！");
    }
    // 查看资源编码有没有重复
    QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .lambda()
        .eq(Resource::getCode, resource.getCode())
        .ne(MyBaseEntity::getId, resource.getId());
    List<Resource> list = list(queryWrapper);
    if (!list.isEmpty()) {
      throw new BusinessException("当前资源编码已存在！");
    }
    QueryWrapper<Resource> queryWrapper1 = new QueryWrapper<>();
    queryWrapper1.lambda().eq(Resource::getParentId, resource.getParentId());
    queryWrapper1.lambda().eq(Resource::getName, resource.getName());
    updateById(resource);
  }

  @Override
  public void removeResourceByIds(List<String> resourceIds) {
    removeBatchByIds(resourceIds);
  }

  @Override
  public void enableResource(Resource resource) {
    if (StringUtils.isNotBlank(resource.getId())) {
      throw new BusinessException("资源id不能为空！");
    }
    resource.setStatus("active");
    updateById(resource);
  }

  @Override
  public void disableResource(Resource resource) {
    if (StringUtils.isNotBlank(resource.getId())) {
      throw new BusinessException("资源id不能为空！");
    }
    resource.setStatus("disabled");
    updateById(resource);
  }
}
