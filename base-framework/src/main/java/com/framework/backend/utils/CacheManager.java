package com.framework.backend.utils;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ALI
 * @since 2023/6/4
 */
@Component
public class CacheManager {
  private static final int TIME_OUT = 4;
  @Autowired private RedisTemplate<String, Object> redisTemplate;

  public Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public <T> T get(String key, Class<T> clazz) {
    Object value = get(key);
    if (value == null) {
      return null;
    }
    return (T) value;
  }

  public void set(String key, Object value) {
    if (value == null) {
      redisTemplate.delete(key);
      return;
    }
    redisTemplate.opsForValue().set(key, value, TIME_OUT, TimeUnit.HOURS);
  }

  public void set(String key, Object value, Long timeOut, TimeUnit timeUnit) {
    if (value == null) {
      redisTemplate.delete(key);
      return;
    }
    redisTemplate.opsForValue().set(key, value, timeOut, timeUnit);
  }

  public boolean containsKey(String key) {
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  public long getExpire(String key) {
    return redisTemplate.getExpire(key);
  }
}
