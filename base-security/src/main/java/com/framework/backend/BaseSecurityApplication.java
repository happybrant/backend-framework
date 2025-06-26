package com.framework.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.framework.backend.mapper.*")
@SpringBootApplication
public class BaseSecurityApplication {

  public static void main(String[] args) {
    SpringApplication.run(BaseSecurityApplication.class, args);
  }
}
