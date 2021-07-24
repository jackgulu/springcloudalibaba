package com.hikvision.com.hikvision.feignapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "nacos-provider")
public interface EchoService {
    @GetMapping("/reactive/helloTest")
    String echo();
}
