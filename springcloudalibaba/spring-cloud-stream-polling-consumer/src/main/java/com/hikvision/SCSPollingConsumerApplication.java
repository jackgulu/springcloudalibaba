package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.binder.PollableMessageSource;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBinding({SCSPollingConsumerApplication.MySink.class})
public class SCSPollingConsumerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder().sources(SCSPollingConsumerApplication.class)
                .web(WebApplicationType.NONE).run(args);
    }
    public interface MySink {

        String INPUT = "input";

        @Input(INPUT)
        PollableMessageSource input();
    }

    @Autowired
    MySink mySink;

    @Bean
    public CommandLineRunner runner() {
        return (args) -> {
            while (true) {
                mySink.input().poll(m -> {
                    System.out.println("poll: " + m.getPayload());
                });
                Thread.sleep(5000L);
            }
        };
    }
}
