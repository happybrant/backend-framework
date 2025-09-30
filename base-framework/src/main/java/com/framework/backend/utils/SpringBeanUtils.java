package com.framework.backend.utils;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author fucong
 * @since 2025/9/30 17:07
 * @description Bean工具类
 */
@Component
public class SpringBeanUtils implements ApplicationContextAware {
  /** -- GETTER -- 获取applicationContext */
  @Getter private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    SpringBeanUtils.applicationContext = applicationContext;
  }

  /**
   * 通过name获取 Bean
   *
   * @param name
   * @return
   */
  public static Object getBean(String name) {
    return getApplicationContext().getBean(name);
  }

  /**
   * 通过class获取Bean
   *
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(Class<T> clazz) {
    return getApplicationContext().getBean(clazz);
  }

  /**
   * 通过name,以及Clazz返回指定的Bean
   *
   * @param name
   * @param clazz
   * @param <T>
   * @return
   */
  public static <T> T getBean(String name, Class<T> clazz) {
    return getApplicationContext().getBean(name, clazz);
  }

  /**
   * 获取环境变量值
   *
   * @param name
   * @return
   */
  public static <T> T getProperty(String name, Class<T> targetType) {
    return getApplicationContext().getEnvironment().getProperty(name, targetType);
  }
}
