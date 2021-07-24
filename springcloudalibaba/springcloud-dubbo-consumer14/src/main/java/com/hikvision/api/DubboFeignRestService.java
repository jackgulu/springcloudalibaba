package com.hikvision.api;

import com.alibaba.cloud.dubbo.annotation.DubboTransported;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("dubbo-provider")
@DubboTransported(protocol = "dubbo")
public interface DubboFeignRestService {

    @GetMapping("/param")
    public String param(@RequestParam String param);


    @PostMapping("/params")
    public String params(@RequestParam int a, @RequestParam String b);
}
