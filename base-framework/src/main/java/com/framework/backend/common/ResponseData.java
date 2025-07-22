package com.framework.backend.common;

import com.framework.backend.enums.ResultCode;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author fucong
 * @since 2023/11/28 17:02
 * @description 返回结果对象
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseData<T> implements Serializable {

  @Serial private static final long serialVersionUID = -4304353934293881342L;

  /** 信息 */
  private String message;

  /** 状态编码 */
  private Integer code;

  /** 数据对象 */
  private T data;

  public ResponseData<T> success(T data) {
    return new ResponseData<>(ResultCode.SUCCESS.getMessage(), ResultCode.SUCCESS.getCode(), data);
  }

  public ResponseData<T> fail(T data) {
    return new ResponseData<>(ResultCode.FAIL.getMessage(), ResultCode.FAIL.getCode(), data);
  }

  public ResponseData(Integer code, String message) {
    this.code = code;
    this.message = message;
  }

  public ResponseData(ResultCode resultCode, T data) {
    this.code = resultCode.getCode();
    this.message = resultCode.getMessage();
    this.data = data;
  }

  public ResponseData(ResultCode resultCode) {
    this.code = resultCode.getCode();
    this.message = resultCode.getMessage();
  }
}
