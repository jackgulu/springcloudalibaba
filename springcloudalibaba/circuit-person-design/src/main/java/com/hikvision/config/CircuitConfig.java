package com.hikvision.config;

import lombok.Data;

@Data
public class CircuitConfig {
    /**
     * closed状态进入open状态的错误个数阈值
     */
    private Integer failureCount=5;
    /**
     * open状态进入hald-open状态的超时时间
     */
    private Long failreTimeInterval=2*1000L;
    /**
     * half-open状态进入open状态的成功个数阈值
     */
    private int halfOpenSuccessCount=2;

    /**
     * Open状态进入Half-open状态的超时时间
     */
    private int halfOpenTimeout=5*1000;
}
