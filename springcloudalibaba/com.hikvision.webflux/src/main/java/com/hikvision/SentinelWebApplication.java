package com.hikvision;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Profile("sccb")
@SpringBootApplication
public class SentinelWebApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SentinelWebApplication.class)
                .properties("spring.profiles.active=sccb").web(WebApplicationType.SERVLET)
                .run(args);
    }

    @RestController
    class SentinelController {
        @Autowired
        private CircuitBreakerFactory circuitBreakerFactory;
        @Autowired
        private RestTemplate restTemplate;

        @RequestMapping("/exp")
        public String exp() {
            StringBuilder sb = new StringBuilder();
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("temp");
            String url = "http://httpbin.org/status/500";
            for (int index = 0; index < 10; index++) {
                String httpResult = circuitBreaker.run(() -> {
                    return restTemplate.getForObject(url, String.class);
                }, throwable -> {
                    if (throwable instanceof DegradeException) {
                        return "degrade by sentinel";
                    }
                    return "exception occurs with url:" + url;
                });
                sb.append(httpResult).append("<br/>");
            }
            return sb.toString();
        }

        @RequestMapping("/rt")
        public String rt() {
            StringBuilder stringBuilder = new StringBuilder();
            CircuitBreaker cb = circuitBreakerFactory.create("rt");
            String url = "http://httpbin.org/delay/3";
            for (int index = 0; index < 10; index++) {
                String httpResult = cb.run(() -> {
                    Map<String, Object> forObject = restTemplate.getForObject(url, Map.class);
                    return (String) forObject.get("origin");
                }, throwable -> {
                    if (throwable instanceof DegradeException) {
                        return "degrade by sentinel";
                    }
                    return "exception occurs with url:" + url;
                });
                stringBuilder.append(httpResult).append("<br/>");
            }
            return stringBuilder.toString();
        }
    }
}
