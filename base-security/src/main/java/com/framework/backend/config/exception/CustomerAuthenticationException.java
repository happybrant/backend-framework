package com.framework.backend.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author fucong
 * @since 2023/11/30 17:07
 * @description 自定义异常 通过传入的异常 可以获取对应的信息返回给前端 用户认证异常
 */
public class CustomerAuthenticationException extends AuthenticationException {
  public CustomerAuthenticationException(String msg) {
    super(msg);
  }
}
