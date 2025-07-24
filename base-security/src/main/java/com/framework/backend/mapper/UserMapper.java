package com.framework.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.framework.backend.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author fucong
 * @description To do
 * @since 2025/6/26 13:50
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {}
