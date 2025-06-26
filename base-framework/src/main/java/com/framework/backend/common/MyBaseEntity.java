package com.framework.backend.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fucong
 * @description 实体基础表
 * @since 2025/6/25 15:43
 */
@Data
@EqualsAndHashCode()
public class MyBaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @TableId(value = "ID")
  protected String id;

  /** 创建用户 */
  @TableField(value = "CREATE_USER", fill = FieldFill.INSERT)
  protected String createUser;

  /** 创建时间 */
  @TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
  protected Date createTime;

  /** 修改用户 */
  @TableField(value = "UPDATE_USER", fill = FieldFill.INSERT_UPDATE)
  protected String updateUser;

  /** 修改时间 */
  @TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
  protected Date updateTime;

  @TableField(exist = false)
  protected Long pageSize;

  @TableField(exist = false)
  protected Long currentPage;
}
