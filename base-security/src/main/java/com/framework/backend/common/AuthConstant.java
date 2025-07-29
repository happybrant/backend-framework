package com.framework.backend.common;

/**
 * @author fucong
 * @since 2025/06/30 17:07
 * @description 认证的常量
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
