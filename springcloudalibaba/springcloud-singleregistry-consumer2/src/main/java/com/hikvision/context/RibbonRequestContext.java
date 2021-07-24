package com.hikvision.context;

import java.util.HashMap;
import java.util.Map;

public class RibbonRequestContext {
    private Map<String,String> attr=new HashMap<>();

    public String put(String key,String value){
        return attr.put(key,value);
    }

    public String remove(String key){
        return attr.remove(key);
    }

    public String get(String key){
        return attr.get(key);
    }
}
