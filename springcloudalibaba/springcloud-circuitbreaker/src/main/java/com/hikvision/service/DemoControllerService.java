package com.hikvision.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DemoControllerService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CircuitBreakerFactory cbFactory;


    public String slow() {
        return cbFactory.create("slow").run(() -> restTemplate.getForObject("/slow", String.class), throwable -> "fallback");
    }
}
