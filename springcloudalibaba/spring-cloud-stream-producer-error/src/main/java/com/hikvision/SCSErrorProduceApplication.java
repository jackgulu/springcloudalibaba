package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableBinding({Source.class})
public class SCSErrorProduceApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SCSErrorProduceApplication.class)
                .web(WebApplicationType.NONE).run(args);
    }

    @Autowired
    private Source source;

    @Bean("test-output.errors")
    MessageChannel testOutputErrorChannel() {
        return new PublishSubscribeChannel();
    }

    @Service
    class ErrorProduceService {
        @ServiceActivator(inputChannel = "test-output.errors")
        public void receiveProduceError(Message message) {
            System.out.println("test-output.errors====receive error msg: " + message);
        }
        @ServiceActivator(inputChannel = "test-output")
        public void receiveProduceError1(Message message) {
            System.out.println("test-output=======receive error msg: " + message);
        }
    }

    @Bean
    public CommandLineRunner runner() {
        return args -> {
            int count = 5;
            for (int index = 1; index <= count; index++) {
                source.output().send(MessageBuilder.withPayload(String.valueOf(index)).build());
            }
        };
    }
}
