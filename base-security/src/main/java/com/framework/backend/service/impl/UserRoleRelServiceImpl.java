package com.framework.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.mapper.UserRoleMapper;
import com.framework.backend.model.entity.UserRoleRel;
import com.framework.backend.service.UserRoleRelService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description 角色服务实现类
 * @since 2025/6/26 13:48
 */
@Service
public class UserRoleRelServiceImpl extends ServiceImpl<UserRoleMapper, UserRoleRel>
    implements UserRoleRelService {

  @Override
  public void bindUserRole(List<UserRoleRel> userRoleRelList) {
    if (!userRoleRelList.isEmpty()) {}
  }
}
