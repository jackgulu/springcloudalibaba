package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient(autoRegister = false)
public class NacosConsumer {
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumer.class,args);
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    @RestController
    @RequestMapping("/noreactive")
    class HelloController{
        @Autowired
        private DiscoveryClient discoveryClient;

        @Autowired
        private RestTemplate restTemplate;


        private String serviceName="nacos-provider";
        @RequestMapping("/info")
        public String info(){
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("All services: "+discoveryClient.getServices()+" <br/>");
            stringBuilder.append("nacos-provider instance list:<br/>");
            instances.forEach(instance->{
                stringBuilder.append("[ serviceId: "+instance.getServiceId()+",host: "+instance.getHost()+" , port:"+instance.getPort()+" ]");
                stringBuilder.append("<br/>");
            });
            return stringBuilder.toString();
        }
        @RequestMapping("/hello")
        public String hello(){
            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            ServiceInstance serviceInstance = instances.stream().findAny().orElseThrow(() -> new IllegalStateException("no " + serviceName + " instance available"));
            return restTemplate.getForObject("http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+"/echo?name=nacos",String.class);
        }
    }
}
