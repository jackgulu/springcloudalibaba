package com.hikvision.service;

public interface LoopLongPollingService {
    String pull();

    String push(String data);
}
