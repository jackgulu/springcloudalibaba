package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableConfigServer
@RefreshScope
public class SpringConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringConfigApplication.class, args);
    }

    @RestController
    @RequestMapping("/testConfig")
    class TestApplication {
        @Autowired
        private Environment environment;


        @RequestMapping("/getEnv")
        public String testDetail() {
            return environment.getProperty("book.category");
        }
    }
}
