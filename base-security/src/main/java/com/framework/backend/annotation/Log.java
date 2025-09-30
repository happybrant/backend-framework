package com.framework.backend.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fucong
 * @description 日志注解
 * @since 2025/9/30 9:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
  /** 操作描述 */
  String value() default "";

  /** 模块名称 */
  String module() default "";
}
