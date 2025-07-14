package com.framework.backend.common;

/**
 * 认证的常量
 *
 * @author ALI
 * @since 2023/6/10
 */
public class AuthConstant {

  public static final String LOGIN_PRE = "login:";
  public static final String CAPTCHA_PRE = "captcha:";

  public static String buildLoginKey(String key) {
    return LOGIN_PRE + key;
  }

  public static String buildCaptchaKey(String key) {
    return CAPTCHA_PRE + key;
  }
}
