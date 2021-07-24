package com.hikvision.dto;

import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
    /**
     * Closed状态进入Open状态的错误个数阈值
     */
    private final int failreCount;
    /**
     * failureCount统计时间窗口
     */
    private final long failureTimeInterval;

    /**
     * 当前错误的次数
     */
    private final AtomicInteger currentCount;

    /**
     * 上次调用失败的时间戳
     */
    private long lastTime;
    /**
     * half-open状态下成功的次数
     */
    private final AtomicInteger halfOpenSuccessCount;


    public Counter(int failreCount, long failureTimeInterval) {
        this.failreCount = failreCount;
        this.failureTimeInterval = failureTimeInterval;
        this.currentCount = new AtomicInteger(0);
        this.halfOpenSuccessCount = new AtomicInteger(0);
        this.lastTime = System.currentTimeMillis();
    }

    public synchronized int incrFailureCount() {
        long current = System.currentTimeMillis();
        if (current - lastTime > failureTimeInterval) {
            lastTime = current;
            currentCount.set(0);
        }
        return currentCount.getAndIncrement();
    }

    public int incrSuccessHalfOpenCount(){
        return this.halfOpenSuccessCount.incrementAndGet();
    }
    public boolean failureThresholdReached(){
        return  getCurCount()>=failreCount;
    }

    public int getCurCount(){
        return currentCount.get();
    }

    public synchronized void reset(){
        halfOpenSuccessCount.set(0);
        currentCount.set(0);
    }
}
