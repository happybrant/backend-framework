package com.framework.backend.config;

import com.framework.backend.config.handler.*;
import jakarta.annotation.Resource;
import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * @author fucong
 * @description SpringSecurity配置类
 * @since 2025/7/9 13:45
 */
@Configuration
@EnableWebSecurity // 启用Spring Security
@EnableMethodSecurity
public class SecurityConfig {

  @Resource private MyLoginSuccessHandler loginSuccessHandler;
  @Resource private MyLoginFailureHandler loginFailureHandler;
  @Resource private MyAuthenticationEntryPoint loginAuthenticationHandler;
  @Resource private MyAccessDeniedHandler loginAccessDefineHandler;
  @Resource private MyLogoutSuccessHandler logoutSuccessHandler;
  @Resource private MyLogoutHandler logoutHandler;
  @Resource private CheckTokenFilter checkTokenFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Value("${base.security.permit.urls}")
  public String permitCustomUrls;

  @Value("${base.security.login.url}")
  public String loginUrl;

  private final String usernameParameter = "account";
  private final String passwordParameter = "password";
  private final String logoutUrl = "/security/user/logout";

  /**
   * 新版的实现方法不再和旧版一样在配置类里面重写方法，而是构建了一个过滤链对象并通过@Bean注解注入到IOC容器中 新版整体代码
   * （注意：新版AuthenticationManager认证管理器默认全局）
   *
   * @param http http安全配置
   * @return SecurityFilterChain
   * @throws Exception 异常
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    String[] publicUrlsArray = {
      "/v3/**",
      "/profile/**",
      "/swagger-ui.html",
      "/swagger-resources/**",
      "/v2/api-docs",
      "/v3/api-docs",
      "/webjars/**",
      "/swagger-ui/**",
      "/v2/**",
      "/favicon.ico",
      "/webjars/springfox-swagger-ui/**",
      "/static/**",
      "/webjars/**",
      "/v2/api-docs",
      "/v2/feign-docs",
      "/swagger-resources/configuration/ui",
      "/swagger-resources",
      "/swagger-resources/configuration/security",
      "/swagger-ui.html",
      "/webjars/**"
    };
    String[] permitAllUrls;
    // 当需要开放自定义的URL时
    if (StringUtils.isNotBlank(permitCustomUrls)) {
      String[] permitCustomUrlsArray = permitCustomUrls.split(",");
      permitAllUrls = ArrayUtils.addAll(publicUrlsArray, permitCustomUrlsArray);
    } else {
      permitAllUrls = publicUrlsArray;
    }

    http // 使用自己自定义的过滤器 去过滤接口请求
        .addFilterBefore(checkTokenFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin(
            formLogin ->
                // 这里更改SpringSecurity的认证接口地址，这样就默认处理这个接口的登录请求了
                formLogin
                    .loginProcessingUrl(loginUrl)
                    .usernameParameter(usernameParameter)
                    .passwordParameter(passwordParameter)
                    // 　自定义的登录验证成功或失败后的去向
                    .successHandler(loginSuccessHandler)
                    .failureHandler(loginFailureHandler))
        .logout(
            logout ->
                logout
                    .logoutUrl(logoutUrl)
                    .addLogoutHandler(logoutHandler)
                    .logoutSuccessHandler(logoutSuccessHandler))
        // 禁用了 CSRF 保护。
        .csrf(AbstractHttpConfigurer::disable)
        // 配置了会话管理策略为 STATELESS（无状态）。在无状态的会话管理策略下，应用程序不会创建或使用 HTTP 会话，每个请求都是独立的，服务器不会在请求之间保留任何状态信息。
        .sessionManagement(
            (sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            (authorizeRequests) ->
                // 这里过滤一些 不需要token的接口地址
                authorizeRequests
                    .requestMatchers(permitAllUrls)
                    .permitAll()
                    .requestMatchers(loginUrl)
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            (exceptionHandling) ->
                exceptionHandling
                    .authenticationEntryPoint(loginAuthenticationHandler) // 匿名处理
                    .accessDeniedHandler(loginAccessDefineHandler) // 无权限处理
            )
        .cors((cors) -> cors.configurationSource(configurationSource()))
        .headers((headers) -> headers.frameOptions((HeadersConfigurer.FrameOptionsConfig::disable)))
        .headers(
            (headers) -> headers.frameOptions((HeadersConfigurer.FrameOptionsConfig::sameOrigin)));
    // 构建过滤链并返回
    return http.build();
  }

  /** 跨域配置 */
  CorsConfigurationSource configurationSource() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
    corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
    corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
    corsConfiguration.setMaxAge(3600L);
    corsConfiguration.setExposedHeaders(Arrays.asList("x-auth-token"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }
}
