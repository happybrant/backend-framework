package com.framework.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.backend.common.MyBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucong
 * @description 授权表
 * @since 2025/7/24 15:38
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("security_authorization")
@Schema(description = "授权表")
public class Authorization extends MyBaseEntity {
  @Serial private static final long serialVersionUID = 1L;

  @Schema(description = "角色外键")
  @TableField("role_id")
  private String roleId;

  @Schema(description = "资源外键")
  @TableField("resource_id")
  private String resourceId;
}
