package com.framework.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.framework.backend.common.MyPage;
import com.framework.backend.mapper.LogMapper;
import com.framework.backend.model.entity.SecurityLog;
import com.framework.backend.service.LogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author fucong
 * @description 角色服务实现类
 * @since 2025/6/26 13:48
 */
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, SecurityLog> implements LogService {

  @Override
  public MyPage<SecurityLog> pageList(SecurityLog log) {
    QueryWrapper<SecurityLog> queryWrapper = new QueryWrapper<>();
    queryWrapper
        .lambda()
        .eq(StringUtils.isNotBlank(log.getModule()), SecurityLog::getModule, log.getModule());
    queryWrapper
        .lambda()
        .like(StringUtils.isNotBlank(log.getContent()), SecurityLog::getContent, log.getContent());
    queryWrapper
        .lambda()
        .like(StringUtils.isNotBlank(log.getAccount()), SecurityLog::getAccount, log.getAccount());
    queryWrapper
        .lambda()
        .ge(log.getStartFromTime() != null, SecurityLog::getStartTime, log.getStartFromTime());
    queryWrapper
        .lambda()
        .le(log.getStartToTime() != null, SecurityLog::getStartTime, log.getStartToTime());
    MyPage<SecurityLog> myPage = new MyPage<>(log.getCurrentPage(), log.getPageSize());
    return baseMapper.selectPage(myPage, queryWrapper);
  }
}
