package com.framework.backend.config.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author fucong
 * @since 2023/11/30 17:07
 * @description 自定义异常 AuthenticationException 是spring security提供的异常 通过传入的异常 可以获取对应的信息返回给前端 token异常
 */
public class TokenException extends AuthenticationException {
  public TokenException(String msg) {
    super(msg);
  }
}
