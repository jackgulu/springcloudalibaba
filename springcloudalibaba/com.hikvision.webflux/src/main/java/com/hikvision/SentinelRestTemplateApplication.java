package com.hikvision;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.hikvision.util.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Profile("resttemplate")
@SpringBootApplication
public class SentinelRestTemplateApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SentinelRestTemplateApplication.class)
                .properties("spring.profiles.active=resttemplate").web(WebApplicationType.SERVLET)
                .run(args);
    }

    @Bean
    @LoadBalanced
    @SentinelRestTemplate(fallbackClass = ExceptionUtils.class, fallback = "handleFallback",
            blockHandler = "handleBlock",blockHandlerClass = ExceptionUtils.class)
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        template.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR;
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                throw new IllegalStateException("illegal status code");
            }
        });
        return template;
    }

    @RestController
    class SentinelController {

        @Autowired
        RestTemplate restTemplate;

        @GetMapping("/exp12")
        public String exp() {
            return restTemplate.getForObject("https://httpbin.org/status/500", String.class);
        }

        @GetMapping("/rt12")
        public String rt() {
            return restTemplate.getForObject("https://httpbin.org/delay/3", String.class);
        }

        @GetMapping("/404")
        public String f04() {
            return restTemplate.getForObject("https://httpbin.org/status/404", String.class);
        }

    }

}