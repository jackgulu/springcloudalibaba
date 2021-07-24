package com.hikvision;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.math.BigDecimal;
import java.util.Random;

@SpringBootApplication
@EnableBinding(Source.class)
@EnableScheduling
public class CreditCardSourceApplication {

    private final Logger logger = LoggerFactory.getLogger(CreditCardSourceApplication.class);

    public static Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(CreditCardSourceApplication.class, args);
    }

    @Autowired
    private Source source;

    @Scheduled(fixedDelay = 2)
    public void sendMsg() {
        int cost = random.nextInt(2000);
        String cardType = CreditCardRecord.cardTypes.get(
                String.valueOf(random.nextInt(CreditCardRecord.cardTypes.size())));
        String user = CreditCardRecord.users.get(String.valueOf(random.nextInt(CreditCardRecord.users.size())));
        CreditCardRecord record = new CreditCardRecord(user, new BigDecimal(cost), cardType);
        logger.info("credit card cost record: " + record);
        source.output().send(MessageBuilder.withPayload(record).build());
    }

}
