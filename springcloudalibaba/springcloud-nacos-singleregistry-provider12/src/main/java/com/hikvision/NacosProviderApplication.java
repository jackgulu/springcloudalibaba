package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@SpringBootApplication
public class NacosProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosProviderApplication.class,args);
    }
    @RestController
    @RefreshScope
    class ConfigurationController {
       @Value("${book.author:unlonwn}")
        String bookAuthor;
        @Autowired
        ApplicationContext applicationContext;
       @Resource
        private BookProperties bookProperties;

        @GetMapping("/config")
        public String config() {
            StringBuilder sb = new StringBuilder();
            sb.append("env.get('book.category')=" + applicationContext.getEnvironment()
                    .getProperty("book.category1", "unknown"))
                    .append("<br/>env.get('book.author')=" + applicationContext.getEnvironment()
                            .getProperty("book.author", "unknown"))
            .append("<br/>bookAuthor="+bookAuthor)
            .append("<br/>bookproperties :"+bookProperties.toString());
            return sb.toString();
        }

    }

}
