package com.framework.backend.common.exception;

import com.framework.backend.enums.ResultCode;

/**
 * @author fucong
 * @description 业务处理异常
 * @since 2025/08/11 15:43
 */
public class BusinessException extends MyBaseException {
  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(ResultCode resultCode) {
    super(resultCode.getMessage());
  }
}
