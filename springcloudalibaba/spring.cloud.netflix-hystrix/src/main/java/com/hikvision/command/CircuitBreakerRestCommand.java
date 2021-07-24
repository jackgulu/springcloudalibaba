package com.hikvision.command;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import org.springframework.web.client.RestTemplate;

public class CircuitBreakerRestCommand extends HystrixCommand<String> {

    private final RestTemplate restTemplate = new RestTemplate();
    private String code;

    public CircuitBreakerRestCommand(String code) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CBRestExample"))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withCircuitBreakerErrorThresholdPercentage(10)
                                .withCircuitBreakerRequestVolumeThreshold(10)
                                .withCircuitBreakerSleepWindowInMilliseconds(3000)
                        //.withMetricsRollingStatisticalWindowInMilliseconds(1000)
                )
        );
        this.code = code;
    }

    @Override
    protected String run() {
        String url = "http://httpbin.org/status/" + code;
        System.out.println("start to curl: " + url);
        restTemplate.getForObject(url, String.class);
        return "Request success";
    }

    @Override
    protected String getFallback() {
        return "Request failed";
    }
}
