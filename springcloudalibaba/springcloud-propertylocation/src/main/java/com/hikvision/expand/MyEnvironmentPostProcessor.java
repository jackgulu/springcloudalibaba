package com.hikvision.expand;

import com.hikvision.property.MyPropertySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put("myName", "lizo");
        MapPropertySource mapPropertySource=new MapPropertySource("myPropertySource",propertyMap);
        environment.getPropertySources().addLast(mapPropertySource);
    }
}
