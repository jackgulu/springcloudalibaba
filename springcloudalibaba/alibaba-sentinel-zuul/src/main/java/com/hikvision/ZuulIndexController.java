package com.hikvision;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZuulIndexController {
    @RequestMapping("/springcloud")
    @SentinelResource(value = "springcloud")
    public String springCloud(){
        return "spring cloud";
    }

    @RequestMapping("/hello")
    @SentinelResource(value = "hello",blockHandler = "handleBlock",fallback = "handleBlock")
    public String hello(){
        return "hello world";
    }

    public String handleBlock(BlockException e){
        e.printStackTrace();
        return e.getMessage();
    }
    @RequestMapping("/hello/{name}")
    @SentinelResource(value = "/hello/{name}")
    public String get(@PathVariable String name){
        return "Hello: "+name;
    }

}
