package com.framework.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.backend.model.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fucong
 * @description To do
 * @since 2025/6/26 13:50
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
  /**
   * 查看角色下用户数量
   *
   * @param roleIds
   * @return
   */
  int selectCountByRoleIds(List<String> roleIds);

  /**
   * 查看角色下的用户列表
   *
   * @param roleId
   * @return
   */
  List<User> selectUserListByRoleId(String roleId);
}
