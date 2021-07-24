package com.hikvision;

import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.BlockResponse;
import com.alibaba.csp.sentinel.adapter.gateway.zuul.fallback.ZuulBlockFallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
public class SentinelZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SentinelZuulApplication.class, args);
    }

    @Bean
    public ZuulBlockFallbackProvider zuulBlockFallbackProvider1() {
         return new ZuulBlockFallbackProvider() {
             @Override
             public String getRoute() {
                 return "*";
             }

             @Override
             public BlockResponse fallbackResponse(String route, Throwable throwable) {
              if(route.equals("my-provider1")){
                  return new BlockResponse(403,"Provider1 Block", route);
              }else if(route.equals("my-provider2")){
                  return new BlockResponse(403, "Provider2 Block", route);
              }else {
                  return new BlockResponse(403, "Sentinel Block", route);
              }
             }
         };
    }

}
