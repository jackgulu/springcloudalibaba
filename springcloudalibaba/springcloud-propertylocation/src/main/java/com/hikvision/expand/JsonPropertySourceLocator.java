package com.hikvision.expand;


import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonPropertySourceLocator implements PropertySourceLocator {
    private final static String DEFAULT_LOCATION = "classpath:my.json";
    @Override
    public PropertySource<?> locate(Environment environment) {
        // TODO 微服务配置中心实现形式即可在这里远程RPC加载配置到spring环境变量中
        // 读取classpath下的my.json解析
        ResourceLoader resourceLoader=new DefaultResourceLoader(JsonPropertySourceLocator.class.getClassLoader());
        Resource resource = resourceLoader.getResource(DEFAULT_LOCATION);
        if(resource==null){
            return null;
        }
        return new MapPropertySource("myjson",mapPropertySource(resource));
    }

    private Map<String, Object> mapPropertySource(Resource resource) {
        Map<String,Object> result=new HashMap<>();
        Map<String,Object> fileMap= JSONObject.parseObject(readFile(resource), Map.class);
        // 组装嵌套json
        processorMap("", result, fileMap);
        return result;
    }
    private void processorMap(String prefix, Map<String, Object> result, Map<String, Object> fileMap){
        if (prefix.length() > 0) {
            prefix += ".";
        }
        for (Map.Entry<String, Object> entrySet : fileMap.entrySet()) {
            if (entrySet.getValue() instanceof Map) {
                processorMap(prefix + entrySet.getKey(), result, (Map<String, Object>) entrySet.getValue());
            } else {
                result.put(prefix + entrySet.getKey(), entrySet.getValue());
            }
        }
    }
    private String readFile(Resource resource) {

        FileInputStream inputStream;
        try {
            inputStream=new FileInputStream(resource.getFile());
            String str = "";
            byte[] readByte = new byte[1024];
            int length;
            while ((length=inputStream.read(readByte))>0){
                str+=new String(readByte,0,length,"UTF-8");
            }
            return str;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
