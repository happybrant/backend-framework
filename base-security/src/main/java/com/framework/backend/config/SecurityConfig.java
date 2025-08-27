package com.framework.backend.config;

import com.framework.backend.config.handler.*;
import jakarta.annotation.Resource;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
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

  private final String usernameParameter = "account";
  private final String passwordParameter = "password";
  private final String logoutUrl = "/security/user/logout";

  @Value("${base.security.permit.urls}")
  public String permitCustomUrls;

  @Value("${base.security.login.url}")
  public String loginUrl;

  @Resource private MyLoginSuccessHandler loginSuccessHandler;
  @Resource private MyLoginFailureHandler loginFailureHandler;
  @Resource private MyAuthenticationEntryPoint loginAuthenticationHandler;
  @Resource private MyAccessDeniedHandler loginAccessDefineHandler;
  @Resource private MyLogoutSuccessHandler logoutSuccessHandler;
  @Resource private MyUserDetailsService userService;

  // @Autowired public AuthenticationProvider myAuthenticationProvider;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /** 配置从http header中获取session id. */
  @Bean
  public HeaderHttpSessionIdResolver headerHttpSessionIdResolver() {
    // HeaderHttpSessionIdResolver.xAuthToken();
    return new HeaderHttpSessionIdResolver("X-Auth-Token");
  }

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

    http // 使用自定义登录过滤器，登录时可以额外支持json数据格式参数
        .addFilterAt(myLoginFilter(), UsernamePasswordAuthenticationFilter.class)
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
        .logout(logout -> logout.logoutUrl(logoutUrl).logoutSuccessHandler(logoutSuccessHandler))
        // 禁用了 CSRF 保护。
        .csrf(AbstractHttpConfigurer::disable)
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
    corsConfiguration.setExposedHeaders(List.of("x-auth-token"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return source;
  }

  /**
   * 使用自定义登录过滤器，登录时可以额外支持json数据格式参数
   *
   * @return
   */
  @Bean
  MyLoginFilter myLoginFilter() {
    MyLoginFilter myLoginFilter = new MyLoginFilter();
    myLoginFilter.setFilterProcessesUrl(loginUrl);
    myLoginFilter.setUsernameParameter(usernameParameter);
    myLoginFilter.setPasswordParameter(passwordParameter);
    myLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
    myLoginFilter.setAuthenticationFailureHandler(loginFailureHandler);
    myLoginFilter.setAuthenticationManager(authenticationManagerBean());
    return myLoginFilter;
  }

  @Bean
  public AuthenticationManager authenticationManagerBean() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userService);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    // 创建ProviderManager，并注入自定义的AuthenticationProvider
    return new ProviderManager(authenticationProvider);
  }
}
