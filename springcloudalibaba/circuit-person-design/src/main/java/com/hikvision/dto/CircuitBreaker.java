package com.hikvision.dto;

import com.hikvision.config.CircuitConfig;
import com.hikvision.constant.State;
import com.hikvision.exception.DegradeException;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 自定义熔断器
 */
public class CircuitBreaker {
    private State state;

    private CircuitConfig config;

    private Counter counter;

    private long lastOpenedTime;

    public CircuitBreaker(CircuitConfig circuitConfig) {
        this.config = circuitConfig;
        this.state = State.CLOSED;
        this.counter = new Counter(circuitConfig.getFailureCount(), circuitConfig.getFailreTimeInterval());
    }

    private boolean halfOpenTimeOut() {
        return System.currentTimeMillis() - lastOpenedTime > config.getHalfOpenTimeout();
    }

    public <T> T run(Supplier<T> toRun, Function<Throwable, T> fallback) {
        try {
            if (state == State.OPEN) {
                //判断Half-open是否超时
                if (halfOpenTimeOut()) {
                    return halfOpenHandle(toRun, fallback);
                }
                return fallback.apply(new DegradeException("degrade by circuit breaker"));
            } else if (state == State.CLOSED) {
                T result = toRun.get();
                closed();
                return result;
            } else {
                return halfOpenHandle(toRun, fallback);
            }
        } catch (Exception e) {
            counter.incrFailureCount();
            /**
             * 错误次数达到阈值，进入open状态
             */
            if (counter.failureThresholdReached()) {
                open();
            }
            return fallback.apply(e);
        }
    }

    private <T> T halfOpenHandle(Supplier<T> toRun, Function<Throwable, T> fallback) {
        try {
            /**
             * CLosed状态进入Half-open状态
             */
            halfOpen();
            T result = toRun.get();
            int halfOpenSuccCount = counter.incrSuccessHalfOpenCount();
            if (halfOpenSuccCount >= this.config.getHalfOpenSuccessCount()) {
                /**
                 * Half-open状态成功的次数到达阈值，进入Closed状态
                 */
                closed();
            }
            return result;
        } catch (Exception e) {
            open();
            return fallback.apply(new DegradeException("degrade by circuit breaker"));
        }
    }

    private void closed() {
        counter.reset();
        state = State.CLOSED;
    }

    private void open() {
        state = State.OPEN;
        lastOpenedTime = System.currentTimeMillis();
    }

    private void halfOpen() {
        state = State.HALF_OPEN;
    }
}
