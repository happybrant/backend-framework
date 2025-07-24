package com.framework.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.mapper.AuthorizationMapper;
import com.framework.backend.model.entity.Authorization;
import com.framework.backend.model.entity.Resource;
import com.framework.backend.service.AuthorizationService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description 角色服务实现类
 * @since 2025/6/26 13:48
 */
@Service
public class AuthorizationServiceImpl extends ServiceImpl<AuthorizationMapper, Authorization>
    implements AuthorizationService {

  @Override
  public List<String> getByUserId(String userId) {
    List<String> permissions = new ArrayList<>();
    List<Resource> authorizations = baseMapper.selectByUserId(userId);
    if (!authorizations.isEmpty()) {
      permissions = authorizations.stream().map(Resource::getCode).toList();
    }
    return permissions;
  }
}
