package com.hikvision.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCircuitBreaker
@Profile("key")
public class HystrixKeyApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixKeyApplication.class)
                .properties("spring.profiles.active=key")
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
    @RestController
    class HystrixController{
        @HystrixCommand(commandKey = "hello", fallbackMethod = "fallback",commandProperties = {
                @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
                @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "0"),
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
        })
        @GetMapping("/test1")
        public String hello1() {
            return new RestTemplate().getForObject("https://httpbin.org/status/200", String.class);
        }
        @HystrixCommand(commandKey = "hello", fallbackMethod = "fallback")
        @GetMapping("/test2")
        public String hello2() {
            return new RestTemplate().getForObject("https://httpbin.org/status/200", String.class);
        }

        @HystrixCommand(threadPoolKey = "singleThread",fallbackMethod = "fallback",
                threadPoolProperties = {
                        @HystrixProperty(name = "coreSize", value = "1")
                })
        @GetMapping("/singleThread")
        public String singleThread() {
            // 只会打印同一个线程名
            System.out.println("--" + Thread.currentThread().getName());
            return new RestTemplate().getForObject("https://httpbin.org/status/200", String.class);
        }


        @HystrixCommand(threadPoolKey = "singleThread",fallbackMethod = "fallback")
        @GetMapping("/singleThread2")
        public String singleThread2() {
            // 只会打印同一个线程名
            System.out.println("--" + Thread.currentThread().getName());
            return new RestTemplate().getForObject("https://httpbin.org/status/200", String.class);
        }

        @HystrixCommand(fallbackMethod = "fallback")
        @GetMapping("/multipleThread")
        public String multipleThread() {
            // 最多 10 个线程名
            System.out.println("--" + Thread.currentThread().getName());
            return new RestTemplate().getForObject("https://httpbin.org/status/200", String.class);
        }

        public String fallback(Throwable throwable) {
            return "Hystrix fallback: " + throwable.getMessage();
        }
    }
}
