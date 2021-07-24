package com.hikvision;

import feign.hystrix.FallbackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    @Bean
    public FallbackFactory fallbackFactory() {
        return new FallbackFactory();
    }

    @FeignClient(name = "delivery-service", fallbackFactory = FallbackFactory.class)
    public interface DeliveryService {

        @GetMapping("/delivery")
        String delivery(@RequestParam("orderId") String orderId);

    }
    class FallbackInventoryService implements DeliveryService{

        @Override
        public String delivery(String orderId) {
            return "delivery service has some problems";
        }
    }
    class FallbackFactory implements feign.hystrix.FallbackFactory{
       private FallbackInventoryService fallbackInventoryService=new FallbackInventoryService();
        @Override
        public Object create(Throwable throwable) {
            return fallbackInventoryService;
        }
    }

    @RestController
    class OrderController {

        @Autowired
        private DeliveryService deliveryService;

        @GetMapping("/order")
        public String order() {
            String orderId = UUID.randomUUID().toString();
            deliveryService.delivery(orderId);
            return "order " + orderId + " generate";
        }

    }
}
