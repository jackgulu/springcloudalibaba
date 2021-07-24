package com.hikvision.configuration;

import com.hikvision.factory.YamlPropertySourceFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(factory = YamlPropertySourceFactory.class,value = "classpath:mydefault.yml")
public class FactoryConfiguration {
}
