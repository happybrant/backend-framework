package com.framework.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author fucong
 * @since 2025/7/14 17:02
 * @description To do
 */
@SpringBootApplication
// @MapperScan("com.framework.backend.*")
public class BaseSecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(BaseSecurityApplication.class, args);
  }
}
