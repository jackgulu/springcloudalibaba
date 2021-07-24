package com.hikvision.property;

import org.springframework.core.env.EnumerablePropertySource;

import java.util.Map;

public class MyPropertySource extends EnumerablePropertySource<Map<String,String>> {
    @Override
    public String[] getPropertyNames() {
        return source.keySet().toArray(new String[source.size()]);
    }

    @Override
    public Object getProperty(String s) {
        return source.get(name);
    }
    public MyPropertySource(String name, Map source) {
        super(name, source);
    }

    public MyPropertySource(String name){
        super(name);
    }
}
