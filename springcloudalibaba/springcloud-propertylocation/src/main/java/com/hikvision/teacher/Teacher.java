package com.hikvision.teacher;

import org.springframework.stereotype.Component;

@Component
public class Teacher {

    private String name = "张老师";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}