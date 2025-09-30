package com.framework.backend.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.format.DateTimeFormatter;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fucong
 * @since 2025/7/14 17:02
 * @description LocalDateTime全局格式处理
 */
@Configuration
public class LocalDateTimeGlobalConfig {
  private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

  /** 配置LocalDateTime类型序列化与反序列化 */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    return builder -> {
      builder.serializers(
          new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
      builder.deserializers(
          new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
    };
  }
}
