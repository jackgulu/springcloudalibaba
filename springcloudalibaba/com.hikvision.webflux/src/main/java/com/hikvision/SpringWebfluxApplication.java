package com.hikvision;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@SpringBootApplication
@EnableCircuitBreaker
@Profile("annotation")
public class SpringWebfluxApplication {
    public static void main(String[] args) throws IOException {
        new SpringApplicationBuilder(SpringWebfluxApplication.class)
                .properties("spring.profiles.active=annotation").web(WebApplicationType.SERVLET)
                .run(args);
    }

    @RestController
    class HystrixController {
        @RequestMapping("/exp")
        @HystrixCommand(groupKey = "ControllerGroup", fallbackMethod = "fallback")
        public String exp() {
            return new RestTemplate().getForObject("https://httpbin.org/status/500", String.class);
        }
        @HystrixCommand(commandKey = "hello", fallbackMethod = "fallback", commandProperties = {
                @HystrixProperty(name = "fallback.enabled", value = "false")
        })
        @RequestMapping("/hello1")
        public String hello1() {
            return new RestTemplate().getForObject("https://httpbin.org/status/500", String.class);
        }

        public String fallback() {
            return "Hystrix fallback";
        }
    }


}
