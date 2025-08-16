package com.framework.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author fucong
 * @since 2025/7/14 17:02
 * @description To do
 */
@SpringBootApplication
@MapperScan("com.framework.backend.mapper")
public class BaseSecurityApplication implements WebMvcConfigurer {

  public static void main(String[] args) {
    SpringApplication.run(BaseSecurityApplication.class, args);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // 配置 Swagger UI 资源路径
    registry
        .addResourceHandler("/swagger-ui/**")
        .addResourceLocations("classpath:/META-INF/resources/webjars/swagger-ui/5.10.3/");
  }
}
