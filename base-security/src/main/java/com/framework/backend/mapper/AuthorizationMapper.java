package com.framework.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.backend.model.entity.Authorization;
import com.framework.backend.model.entity.Resource;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fucong
 * @description To do
 * @since 2025/6/26 13:50
 */
@Mapper
public interface AuthorizationMapper extends BaseMapper<Authorization> {

  List<Resource> selectByUserId(String userId);
}
