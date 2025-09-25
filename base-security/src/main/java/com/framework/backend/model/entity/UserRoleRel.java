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
 * @description 用户角色关联表
 * @since 2025/6/25 16:04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("security_user_role_rel")
@Schema(description = "用户角色关联表")
public class UserRoleRel extends MyBaseEntity {
  @Serial private static final long serialVersionUID = 1L;

  @Schema(description = "用户外键")
  @TableField("user_id")
  private String userId;

  @Schema(description = "角色外键")
  @TableField("role_id")
  private String roleId;
}
