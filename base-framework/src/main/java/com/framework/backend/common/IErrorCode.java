package com.framework.backend.common;

/**
 * @author fucong
 * @description 错误码
 * @since 2025/08/11 18:45
 */
public interface IErrorCode {
  /** 返回码 */
  int getCode();

  /** 返回信息 */
  String getMessage();
}
