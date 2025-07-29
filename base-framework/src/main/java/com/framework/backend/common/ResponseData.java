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

  /** 成功返回结果 */

  public ResponseData(Integer code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }


  /**
   * 成功返回结果
   */
  public static <T> ResponseData<T> success() {
    return new ResponseData<>(ResultCode.SUCCESS, null);
  }

  /**
   * 成功返回结果
   *
   * @param data 获取的数据
   */
  public static <T> ResponseData<T> success(T data) {
    return new ResponseData<>(ResultCode.SUCCESS, data);
  }

  /**
   * 成功返回结果
   *
   * @param data 获取的数据
   * @param message 提示信息
   */
  public static <T> ResponseData<T> success(T data, String message) {
    return new ResponseData<>(ResultCode.SUCCESS.getCode(), message, data);
  }

  /**
   * 失败返回结果
   *
   * @param errorCode 错误码
   */
  public static <T> ResponseData<T> failed(IErrorCode errorCode) {
    return new ResponseData<>(errorCode.getCode(), errorCode.getMessage(), null);
  }

  /**
   * 失败返回结果
   *
   * @param errorCode 错误码
   * @param message 错误信息
   */
  public static <T> ResponseData<T> failed(IErrorCode errorCode, String message) {
    return new ResponseData<>(errorCode.getCode(), message, null);
  }

  /**
   * 失败返回结果
   *
   * @param message 提示信息
   */
  public static <T> ResponseData<T> failed(String message) {
    return new ResponseData<>(ResultCode.FAILED.getCode(), message, null);
  }

  /** 失败返回结果 */
  public static <T> ResponseData<T> failed() {
    return failed(ResultCode.FAILED);
  }
}
