package com.framework.backend.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.framework.backend.model.entity.User;
import com.framework.backend.utils.SecurityUtils;
import java.time.LocalDateTime;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author fucong
 * @since 2025/07/11 17:07
 * @description mybatis-plus 默认字段填充策略
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
  private static final Logger logger = LoggerFactory.getLogger(MyMetaObjectHandler.class);

  @Override
  public void insertFill(MetaObject metaObject) {
    logger.debug("start insert fill ....");
    // 注意这里的fieldName是实体字段名称，而不是数据库字段名称！
    this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    this.strictInsertFill(metaObject, "lastUpdateTime", LocalDateTime.class, LocalDateTime.now());
    // 存在用户登录信息才自动填值
    User user = SecurityUtils.getCurrentUser();
    if (user != null) {
      String userId = user.getId();
      this.strictInsertFill(metaObject, "createUser", String.class, userId);
      this.strictInsertFill(metaObject, "lastUpdateUser", String.class, userId);
    }
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    logger.debug("start update fill ....");
    this.strictUpdateFill(metaObject, "lastUpdateTime", LocalDateTime.class, LocalDateTime.now());
    // 存在用户登录信息才自动填值，方便定时任务之类不需要登录的功能使用
    User user = SecurityUtils.getCurrentUser();
    if (user != null) {
      String userId = user.getId();
      this.strictUpdateFill(metaObject, "lastUpdateUser", String.class, userId);
    }
  }

  /**
   * 默认填充策略为如果属性有值则不覆盖,现在改为每次都覆盖更新
   *
   * @param metaObject
   * @param fieldName
   * @param fieldVal
   * @return
   */
  @Override
  public MetaObjectHandler fillStrategy(MetaObject metaObject, String fieldName, Object fieldVal) {
    setFieldValByName(fieldName, fieldVal, metaObject);
    return this;
  }
}
