package com.framework.backend.dict.config;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fucong
 * @description 字典集注解
 * @since 2025/9/25 16:27
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = DicSetJsonSerializer.class)
public @interface DicSet {
  String name() default "";

  String format() default "";

  String desc() default "";
}
