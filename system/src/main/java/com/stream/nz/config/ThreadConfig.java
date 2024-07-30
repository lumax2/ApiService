package com.stream.nz.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@ComponentScan(basePackages = {"com.smcv.oversea"})
@EnableAsync
public class ThreadConfig implements AsyncConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(ThreadConfig.class);

    private int core = 5;
    private int max = 50;

    private int capacity = 500;

    private int alive = 30;

    @Override
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程数
        taskExecutor.setCorePoolSize(core);
        // 最大线程数
        taskExecutor.setMaxPoolSize(max);
        // 队列最大长度
        taskExecutor.setQueueCapacity(capacity);
        // 线程池维护线程所允许的空闲时间(单位秒)
        taskExecutor.setKeepAliveSeconds(alive);
        // 线程池对拒绝任务(无线程可用)的处理策略 ThreadPoolExecutor.CallerRunsPolicy策略 ,调用者的线程会执行该任务,如果执行器已关闭,则丢弃.
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        logger.warn("Get in AsyncUncaughtExceptionHandler !");
        return null;
    }
}
