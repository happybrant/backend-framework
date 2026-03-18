package com.framework.backend.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author fucong
 * @since 2025/7/14 17:02
 * @description Redis配置
 */
@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);

    // 1. 字符串序列化器（Key/HashKey）
    RedisSerializer<String> stringSerializer = new StringRedisSerializer();

    // 2. JSON 序列化器（核心变更：构造器传入 ObjectMapper，替代 setObjectMapper）
    ObjectMapper objectMapper = getObjectMapper();
    // 官方推荐：直接通过构造器初始化 Jackson2JsonRedisSerializer，不再调用 setObjectMapper
    Jackson2JsonRedisSerializer<Object> jacksonSerializer =
        new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);

    // 3. 绑定序列化器
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setValueSerializer(jacksonSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);
    redisTemplate.setHashValueSerializer(jacksonSerializer);

    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Bean
  public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
    StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
    stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
    return stringRedisTemplate;
  }

  /** 抽离 ObjectMapper 配置，复用性更高 Spring Boot 3.x 兼容的 TypeValidator 配置 */
  private ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    // 配置类型校验器（替代过时的 LaissezFaireSubTypeValidator）
    var typeValidator =
        BasicPolymorphicTypeValidator.builder()
            // 允许所有 Object 子类保留类型信息
            .allowIfBaseType(Object.class)
            .build();

    // 序列化配置：访问所有字段 + 保留泛型类型
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    objectMapper.activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL);
    return objectMapper;
  }
}
