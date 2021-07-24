package com.hikvision;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("flowcontrol")
@SpringBootApplication
@EnableCircuitBreaker
public class HystrixFlowControlApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixFlowControlApplication.class)
                .properties("spring.profiles.active=flowcontrol").web(WebApplicationType.SERVLET)
                .run(args);
    }

    @RestController
    class HystrixController {

        @HystrixCommand(commandKey = "Hello", groupKey = "HelloGroup", fallbackMethod = "fallback")
        @GetMapping("/hello")
        public String hello() {
            return "Hello World";
        }

        public String fallback(Throwable throwable) {
            return "Hystrix fallback";
        }

    }
}
