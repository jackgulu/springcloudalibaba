package com.hikvision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private Environment environment;
    @GetMapping("/hello")
    public String hello(){
        return environment.getProperty("my.json.property");
    }


    @GetMapping("/process")
    public String process(){
        environment.getProperty("myName");
        return environment.getProperty("myName");
    }


    @GetMapping("getYml")
    public String getYaml(){
        String schoolName= environment.getProperty("school.name");
        String schoolAge=environment.getProperty("school.age");
        String schoolLocation=environment.getProperty("school.location");
        return schoolName+":"+schoolLocation+":"+schoolAge;
    }
}
