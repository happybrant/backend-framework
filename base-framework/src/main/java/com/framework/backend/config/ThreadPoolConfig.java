package com.framework.backend.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author fucong
 * @since 2026/2/6 14:02
 * @description 自定义线程池
 */
@Configuration
public class ThreadPoolConfig {
  @Bean("asyncExecutor")
  public ThreadPoolTaskExecutor asyncExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    // 核心线程数（常驻线程）
    executor.setCorePoolSize(5);
    // 最大线程数（核心线程满后，可创建的最大线程数）
    executor.setMaxPoolSize(20);
    // 队列容量（核心线程满后，任务进入队列等待）
    executor.setQueueCapacity(100);
    // 线程空闲时间（超过该时间，非核心线程被销毁）
    executor.setKeepAliveSeconds(60);
    // 线程名称前缀（便于日志排查）
    executor.setThreadNamePrefix("async-task-");
    // 拒绝策略（线程池+队列都满时，如何处理新任务）
    // CallerRunsPolicy：由调用方线程执行（如主线程），避免任务丢失
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    // 初始化线程池（必须调用，否则不生效）
    executor.initialize();
    return executor;
  }
}
