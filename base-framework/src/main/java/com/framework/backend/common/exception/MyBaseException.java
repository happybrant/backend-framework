package com.framework.backend.common.exception;

import com.framework.backend.enums.ResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author fucong
 * @description 异常基础类
 * @since 2025/08/11 15:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyBaseException extends RuntimeException {
  protected ResultCode resultCode;

  public MyBaseException() {
    super();
  }

  public MyBaseException(String message) {
    super(message);
  }

  public MyBaseException(ResultCode resultCode) {
    super(resultCode.getMessage());
    this.resultCode = resultCode;
  }
}
