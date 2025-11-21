package com.framework.backend.attachment.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.backend.common.MyBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucong
 * @description To do
 * @since 2025/10/10 13:35
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("utility_attachment")
@Schema(description = "附件表")
public class Attachment extends MyBaseEntity {
  @Serial private static final long serialVersionUID = 1L;

  @Schema(description = "文件现名称")
  @TableField(value = "name", keepGlobalFormat = true)
  private String name;

  @Schema(description = "文件原名称")
  @TableField("origin_name")
  private String originName;

  /** 在已有的基础路径上添加文件夹 */
  @Schema(description = "文件存储地址")
  @TableField("location")
  private String location;

  @Schema(description = "文件存储方式/模式")
  @TableField("storage_mode")
  private String storageMode;

  @Schema(description = "文件大小，默认KB")
  @TableField("file_size")
  private Long fileSize;

  @Schema(description = "附件绝对路径")
  @TableField("full_path")
  private String fullPath;

  @Schema(description = "附件相对路径")
  @TableField(exist = false)
  private String relativeUrl;
}
