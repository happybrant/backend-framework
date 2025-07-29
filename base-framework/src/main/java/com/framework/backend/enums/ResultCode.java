package com.framework.backend.enums;

import com.framework.backend.common.IErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author fucong
 * @since 2025/07/09 17:02
 * @description 结果码
 */
@Getter
public enum ResultCode implements IErrorCode {
  /** 成功 */
  SUCCESS(HttpStatus.OK.value(), "操作成功"),
  /** 失败 */
  FAILED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统内部错误"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "未授权访问"),
  FORBIDDEN(HttpStatus.FORBIDDEN.value(), "禁止访问"),
  LOGIN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户账号或密码输入错误"),
  CAPTCHA_FAIL(HttpStatus.INTERNAL_SERVER_ERROR.value(), "验证码输入错误"),
  PASSWORD_EXPIRED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户密码过期错误"),
  LOGIN_SUCCESS(HttpStatus.OK.value(), "用户登录成功"),
  LOGOUT_SUCCESS(HttpStatus.OK.value(), "用户登出成功");

  private final Integer code;
  private final String message;

  ResultCode(Integer code, String message) {
    this.code = code;
    this.message = message;
  }
}
