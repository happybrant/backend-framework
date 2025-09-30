package com.framework.backend.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.framework.backend.common.MyBaseEntity;
import com.framework.backend.dict.config.DicSet;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author fucong
 * @description 日志表
 * @since 2025/7/24 15:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("security_log")
@Schema(description = "日志表")
public class SecurityLog extends MyBaseEntity {

  @Serial private static final long serialVersionUID = 1L;

  @Schema(description = "操作内容简述")
  @TableField("content")
  private String content;

  @Schema(description = "请求开始时间")
  @TableField("start_time")
  private LocalDateTime startTime;

  @Schema(description = "请求结束时间")
  @TableField("end_time")
  private LocalDateTime endTime;

  @Schema(description = "请求耗时")
  @TableField("cost")
  private long cost;

  @Schema(description = "用户账号")
  @TableField(value = "account", keepGlobalFormat = true)
  private String account;

  @Schema(description = "请求IP")
  @TableField("request_ip")
  private String requestIp;

  @Schema(description = "模块名称")
  @TableField("module")
  private String module;

  @Schema(description = "请求客户端")
  @TableField("agent")
  private String agent;

  @Schema(description = "返回结果标识")
  @TableField("result_flag")
  @DicSet(name = "resultFlag", desc = "结果标识")
  private String resultFlag;

  @Schema(description = "错误信息")
  @TableField("error_msg")
  private String errorMsg;

  @TableField(exist = false)
  @DateTimeFormat
  private LocalDateTime startFromTime;

  @TableField(exist = false)
  private LocalDateTime startToTime;
}
