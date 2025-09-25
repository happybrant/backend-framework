package com.framework.backend.dict.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.backend.common.MyBaseEntity;
import com.framework.backend.dict.config.DicSet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucong
 * @description To do
 * @since 2025/8/28 19:17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("utility_dict")
@Schema(description = "字典表")
public class Dict extends MyBaseEntity {
  @Serial private static final long serialVersionUID = 1L;

  @NotBlank(message = "字典编码不能为空")
  @Schema(description = "字典项编码")
  @TableField(value = "code", keepGlobalFormat = true)
  private String code;

  @NotBlank(message = "字典名称不能为空")
  @Schema(description = "字典项名称")
  @TableField(value = "name", keepGlobalFormat = true)
  private String name;

  @NotBlank(message = "父字典项不能为空")
  @Schema(description = "父字典项id")
  @TableField("parent_id")
  private String parentId;

  @NotBlank(message = "字典集分组编码不能为空")
  @Schema(description = "字典集分组编码")
  @TableField("group_code")
  private String groupCode;

  @Schema(description = "状态")
  @TableField(value = "status", keepGlobalFormat = true)
  @DicSet(name = "status", desc = "状态")
  private String status;

  @Schema(description = "描述")
  @TableField(value = "description")
  private String description;

  @Schema(description = "排序编号")
  @TableField("sort")
  private Integer sort;
}
