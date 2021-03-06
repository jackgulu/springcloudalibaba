/*
 * Copyright (C) 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hikvision;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.netflix.hystrix.HystrixCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * @author <a href="mailto:fangjian0423@gmail.com">Jim</a>
 */
@Profile("sccb")
@SpringBootApplication
public class HystrixSpringCloudCircuitBreakerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(HystrixSpringCloudCircuitBreakerApplication.class)
            .properties("spring.profiles.active=sccb").web(WebApplicationType.SERVLET)
            .run(args);
    }

    @RestController
    class HystrixController {
        @Autowired
        CircuitBreakerFactory circuitBreakerFactory;

        @Autowired
        RestTemplate restTemplate;

        @GetMapping("/exp")
        public String exp() {
            StringBuilder sb = new StringBuilder();
            CircuitBreaker cb = circuitBreakerFactory.create("temp");
            String url = "https://httpbin.org/status/500";
            for (int index = 0; index < 10; index++) {
                String httpResult = cb.run(() -> {
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

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Customizer<HystrixCircuitBreakerFactory> customizer() {
        return factory -> {
            factory.configureDefault(id -> HystrixCommand.Setter.withGroupKey(
                HystrixCommandGroupKey.Factory.asKey(id))
                .andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter()
                        .withMetricsRollingStatisticalWindowInMilliseconds(1000)
                        .withCircuitBreakerRequestVolumeThreshold(3)
                        .withCircuitBreakerErrorThresholdPercentage(100)
                        .withCircuitBreakerSleepWindowInMilliseconds(10000)
                        .withExecutionTimeoutInMilliseconds(5000)
                )
            );
        };
    }

}
