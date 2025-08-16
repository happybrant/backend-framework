package com.framework.backend.common.result;

import java.lang.annotation.*;

/**
 * @author fucong
 * @description 开启自定包装返回结果注解
 * @since 2025/08/12 15:43
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
public @interface ResponseResult {
  boolean autoPackage() default true;
}
