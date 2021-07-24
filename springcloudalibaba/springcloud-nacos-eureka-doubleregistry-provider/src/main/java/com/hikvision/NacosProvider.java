package com.hikvision;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@EnableConfigurationProperties
@EnableDiscoveryClient
public class NacosProvider {
    public static void main(String[] args) {
        SpringApplication.run(NacosProvider.class,args);
    }
    @RestController
    class EchoController{
        @GetMapping("/echo")
        public String echo(HttpServletRequest request){
            return "echo: "+request.getParameter("name");
        }
    }
}
