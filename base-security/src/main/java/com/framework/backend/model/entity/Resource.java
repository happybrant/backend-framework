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
 * @description 资源表，菜单、按钮等
 * @since 2025/7/24 15:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("security_resource")
@Schema(description = "资源表")
public class Resource extends MyBaseEntity {

  @Serial private static final long serialVersionUID = 1L;

  @NotBlank(message = "资源名称不能为空")
  @Schema(description = "资源名称")
  @TableField(value = "name", keepGlobalFormat = true)
  private String name;

  @NotBlank(message = "资源编码不能为空")
  @Schema(description = "资源编码")
  @TableField(value = "code", keepGlobalFormat = true)
  private String code;

  @Schema(description = "父资源id")
  @TableField("parent_id")
  private String parentId;

  @NotBlank(message = "资源分类不能为空")
  @Schema(description = "资源分类：menu-菜单,button-按钮")
  @TableField(value = "type", keepGlobalFormat = true)
  private String type;

  @Schema(description = "状态")
  @TableField(value = "status", keepGlobalFormat = true)
  private String status;

  @Schema(description = "排序编号")
  @TableField("sort")
  private Integer sort;
}
