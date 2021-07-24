package com.hikvision;

import com.hikvision.configuration.MyLoadBalancerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.netflix.ribbon.RibbonLoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient(autoRegister = false)
@RestController
@LoadBalancerClients(defaultConfiguration = MyLoadBalancerConfiguration.class)
public class NacosConsumerApplication {
    @Autowired
    private RestTemplate template;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RandomServiceInstanceChooser randomServiceInstanceChooser;
    private String serviceName="nacos-provider";
    @GetMapping("/testLoadBalance")
    public String loadBalanceTest(){
        ServiceInstance choose = loadBalancerClient.choose(serviceName);
        return template.getForObject("http://nacos-provider"+":"+choose.getPort()+"/reactive/helloTest",String.class);

    }
    @GetMapping("/custeomChooser")
    public String chooseChoose(){
        ServiceInstance choose = randomServiceInstanceChooser.choose(serviceName);
        return template.getForObject("http://"+choose.getServiceId()+"/reactive/helloTest",String.class);
    }
   /* @Configuration
    @org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient(name = "nacos-provider", configuration = MyLoadBalancerConfiguration.class)
    class LoadBalanceConfiguration {

    }*/
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate=new RestTemplate();
        return restTemplate;
    }
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class,args);
    }
}
