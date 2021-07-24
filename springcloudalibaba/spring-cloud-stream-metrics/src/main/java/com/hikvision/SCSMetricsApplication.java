package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.support.MessageBuilder;

@SpringBootApplication
@EnableBinding({Source.class})
public class SCSMetricsApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SCSMetricsApplication.class)
                .web(WebApplicationType.SERVLET).run(args);
    }

    @Autowired
    private Source source;

    @Bean
    public CommandLineRunner runner() {
        return (args) -> {
            source.output().send(MessageBuilder.withPayload("msg1")
                    .build());
            source.output().send(MessageBuilder.withPayload("msg2")
                    .build());
            source.output().send(MessageBuilder.withPayload("msg3")
                    .build());
        };
    }

}