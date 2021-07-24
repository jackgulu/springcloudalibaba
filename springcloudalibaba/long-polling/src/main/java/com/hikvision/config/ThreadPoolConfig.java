package com.hikvision.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ScheduledExecutorService getScheduledExecutorService() {
        AtomicInteger poolNum = new AtomicInteger(0);
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(2, r -> {
            Thread thread = new Thread(r);
            thread.setName("LoopLongPollingThread-" + poolNum.incrementAndGet());
            return thread;
        });
        return scheduledExecutorService;
    }
}
