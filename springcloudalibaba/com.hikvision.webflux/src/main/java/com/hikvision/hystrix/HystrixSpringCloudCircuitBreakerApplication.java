package com.hikvision.hystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Profile("sccb")
public class HystrixSpringCloudCircuitBreakerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixSpringCloudCircuitBreakerApplication.class)
                .properties("spring.profiles.active=sccb").web(WebApplicationType.SERVLET)
                .run(args);
    }
   @Bean
   public RestTemplate restTemplate(){return  new RestTemplate();}
    @RestController
    class HystrixController {
        @Autowired
        private CircuitBreakerFactory circuitBreakerFactory;

        @Autowired
        RestTemplate restTemplate;

        @GetMapping("/test3")
        public String exp() {
            StringBuilder sb = new StringBuilder();
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("temp");
            String url = "https://httpbin.org/status/500";
            for (int index = 0; index < 10; index++) {
                String httpResult = circuitBreaker.run(() -> {
                    return restTemplate.getForObject(url, String.class);
                }, throwable -> {
                    if (throwable instanceof HttpServerErrorException) {
                        return "exception occurs with url: " + url;
                    }
                    return "degrade by hystrix";
                });
                sb.append(httpResult).append("<br/>");
            }
            return sb.toString();
        }
    }
}
