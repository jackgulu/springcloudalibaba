package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableBinding({Source.class, Sink.class})
public class SCSErrorConsumeApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SCSErrorConsumeApplication.class)
                .web(WebApplicationType.NONE).run(args);
    }


    @Service
    class ErrorConsumeService {
        @StreamListener(Sink.INPUT)
        public void receive(String receiveMsg) {
            throw new RuntimeException("Oops");
        }

        @ServiceActivator(inputChannel = "test-input.test-input-group.errors")
        public void receiveConsumeError(Message receiveMsg) {
            System.out.println("receive error msg: " + receiveMsg);
        }
    }

    @Autowired
    private Source source;

    @Bean
    public CommandLineRunner commandLineRunner(){
        return args -> {
            int count = 3;
            for (int index = 1; index <= count; index++) {
                source.output().send(MessageBuilder.withPayload("msg-" + index).build());
            }
        };
    }
}
