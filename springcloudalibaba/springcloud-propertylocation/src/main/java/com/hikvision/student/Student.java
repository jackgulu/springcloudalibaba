package com.hikvision.student;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Student {

    @Value("${jdbc.user}")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int age=20;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}