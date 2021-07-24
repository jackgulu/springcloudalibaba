package com.hikvision;

import com.alibaba.cloud.dubbo.annotation.DubboTransported;
import com.hikvision.api.RestService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DubboApplication {

    @Reference(version = "1.0.0", protocol = "dubbo")
    private RestService restService;
    @Bean
    @LoadBalanced
    @DubboTransported
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    public static void main(String[] args) {
        SpringApplication.run(DubboApplication.class,args);
    }
    @RestController
   class TestApplication{
     @Autowired
      private RestTemplate restTemplate;
       @GetMapping("/test")
       public ResponseEntity<String> test(){
           return restTemplate.getForEntity("http://dubbo-provider/param?param=deepingspringcloud",String.class);
       }

       @GetMapping("/dubbo")
        public String dubbo(){
           return restService.param("顾路");
       }
   }
}
