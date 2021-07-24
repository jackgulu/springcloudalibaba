/*
package com.hikvision.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ReactiveControllerService {
    @Autowired
    private WebClient webclient;
    @Autowired
    private ReactiveCircuitBreakerFactory reactiveCircuitBreakerFactory;

    public Mono<String> slow(){
        return webclient.get().uri("/uri").retrieve().bodyToMono(String.class).transform(it->{
            ReactiveCircuitBreaker reactiveCircuitBreaker = reactiveCircuitBreakerFactory.create("slow");
            return reactiveCircuitBreaker.run(it,throwable -> Mono.just("fallback"));
        });
    }
}
*/
