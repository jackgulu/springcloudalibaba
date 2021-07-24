package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.event.EnvironmentChangeRemoteApplicationEvent;
import org.springframework.cloud.context.environment.EnvironmentManager;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class EnvironmentChangeListener implements ApplicationListener<EnvironmentChangeRemoteApplicationEvent> {
    @Autowired
    private EnvironmentManager environmentManager;
    @Override
    public void onApplicationEvent(EnvironmentChangeRemoteApplicationEvent event) {
        Map<String, String> values = event.getValues();
        for (Map.Entry<String,String> entry:values.entrySet()){
            System.out.println(entry.getKey()+":"+entry.getValue());
            environmentManager.setProperty(entry.getKey(),entry.getValue());
        }
    }
}
