package com.hikvision;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SpringBootApplication
@EnableBinding(Sink.class)
public class CreditCardSinkApplication {
    private final Logger logger = LoggerFactory.getLogger(CreditCardSinkApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CreditCardSinkApplication.class, args);
    }

    private Map<String, BigDecimal> lastCostInfo=new HashMap<>();
    private Set<String> blackList=new HashSet<>();
    private BigDecimal  warningMoney=new BigDecimal(2000);

    @StreamListener(Sink.INPUT)
    public void receive(CreditCardRecord record) {
        if (blackList.contains(record.getUser())) {
            logger.info(record.getUser() + " now is in black list");
            return;
        }
        logger.info(record.getUser() + " cost " + record.getCost() + " with " + record.getCardType());
        if (lastCostInfo.containsKey(record.getUser())) {
            BigDecimal recentlyCostMoney = lastCostInfo.get(record.getUser()).add(record.getCost());
            if (recentlyCostMoney.compareTo(warningMoney) >= 0) {
                logger.warn(record.getUser() + " recently cost " + recentlyCostMoney + ", go to black list");
                blackList.add(record.getUser()); // 超过 2000，加入黑名单
                return;
            }
        }
        lastCostInfo.put(record.getUser(), record.getCost());
    }

}
