package com.hikvision;

import com.hikvision.dto.User;
import com.hikvision.selector.MyPartitionSelector;
import com.hikvision.strategy.MyPartitionKeyExtractor;
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
public class SCSProducerPartitionApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SCSProducerPartitionApplication.class)
                .web(WebApplicationType.NONE).run(args);
    }

    @Bean
    MyPartitionSelector mySelector() {
        return new MyPartitionSelector();
    }

    @Bean
    MyPartitionKeyExtractor myKeyExtractor() {
        return new MyPartitionKeyExtractor();
    }

    @Autowired
    private Source source;

    @Bean
    public CommandLineRunner runner() {
        return (args) -> {
            //source.output().send(MessageBuilder.withPayload("msg1")
            //    .setHeader("partitionKey", "test")
            //    .build());
            source.output().send(MessageBuilder.withPayload("msg1")
                    .setHeader("partitionKey", new User(1, "deep in spring cloud"))
                    .build());
        };

    }
}
