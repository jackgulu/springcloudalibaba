package com.hikvision;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("openfeign")
@SpringBootApplication
@EnableFeignClients
public class SentinelOpenFeignApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SentinelOpenFeignApplication.class)
                .properties("spring.profiles.active=openfeign").web(WebApplicationType.SERVLET)
                .run(args);
    }

    @FeignClient(name = "inventory-provider", fallbackFactory = FallbackSentinelFactory.class)
    public interface InventoryService {

        @GetMapping("/save")
        String save();

    }

    @FeignClient(name = "buy-service", url = "https://httpbin.org/delay/3", fallbackFactory = FallbackSentinelFactory.class)
    public interface BuyService {

        @GetMapping
        String buy();

    }

    @Bean
    public FallbackSentinelFactory fallbackFactory() {
        return new FallbackSentinelFactory();
    }

    class FallbackSentinelFactory implements feign.hystrix.FallbackFactory {

        private FallbackService fallbackService = new FallbackService();

        private DefaultBusinessService defaultBusinessService = new DefaultBusinessService();

        @Override
        public Object create(Throwable cause) {
            if (cause instanceof BlockException) {
                return fallbackService;
            } else {
                return defaultBusinessService;
            }
        }
    }

    class FallbackService implements BuyService, InventoryService {
        @Override
        public String buy() {
            return "buy degrade by sentinel";
        }

        @Override
        public String save() {
            return "save degrade by sentinel";
        }
    }

    class DefaultBusinessService implements BuyService, InventoryService {
        @Override
        public String buy() {
            return "buy error";
        }

        @Override
        public String save() {
            return "inventory save error";
        }

    }

    @RestController
    class SentinelController {

        @Autowired
        InventoryService inventoryService;

        @Autowired
        BuyService buyService;

        @GetMapping("/save")
        public String save() {
            return inventoryService.save();
        }

        @GetMapping("/buy")
        public String buy() {
            return buyService.buy();
        }

    }

}
