package com.hikvision.api;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
@Service(version = "1.0.0")
@RestController
public class UserServiceImpl implements RestService{

    @Override
    @GetMapping("/param")
    public String param(@RequestParam String param) {
        return param;
    }

    @Override
    @PostMapping("/params")
    public String params(@RequestParam int a, @RequestParam String b) {
        return a+b;
    }
}
