package com.hikvision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@EnableDiscoveryClient
public class SMSApplication {

    public static void main(String[] args) {
        SpringApplication.run(SMSApplication.class, args);
    }

    @RestController
    class SMSController {

        private AtomicInteger count = new AtomicInteger();

        @GetMapping("/send")
        public String send(String orderId, int delaySecs) {
            int num = count.addAndGet(1);
            if(num >= 1000) {
                if(delaySecs > 0) {
                    try {
                        Thread.sleep(1000 * delaySecs);
                    } catch (InterruptedException e) {
                        return "Interrupted: " + e.getMessage();
                    }
                }
            }
            System.out.println(orderId + " send successfully");
            return "success";
        }

    }

}
