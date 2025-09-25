package com.framework.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.backend.common.MyBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucong
 * @description 角色表
 * @since 2025/6/25 15:43
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("security_role")
@Schema(description = "角色表")
public class Role extends MyBaseEntity {
  @Serial private static final long serialVersionUID = 1L;

  @NotBlank(message = "角色名称不能为空")
  @Schema(description = "角色名")
  @TableField(value = "name", keepGlobalFormat = true)
  private String name;

  @NotBlank(message = "角色编码不能为空")
  @Schema(description = "角色编码")
  @TableField(value = "code", keepGlobalFormat = true)
  private String code;

  @Schema(description = "状态")
  @TableField("status")
  private String status;

  @Schema(description = "备注")
  @TableField("remark")
  private String remark;
}
