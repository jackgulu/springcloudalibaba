package com.hikvision.config;

import com.alibaba.cloud.circuitbreaker.sentinel.SentinelCircuitBreakerFactory;
import com.alibaba.cloud.circuitbreaker.sentinel.SentinelConfigBuilder;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

//@Configuration
public class SentinelConfiguration {

    @Bean
    public Customizer<SentinelCircuitBreakerFactory> customizer(){
        return factory->{
            factory.configureDefault(id->new SentinelConfigBuilder().resourceName(id)
            .rules(Collections.singletonList(new DegradeRule(id).setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT).setCount(3).setTimeWindow(8))).build());
            factory.configure(builder->{
                builder.rules(Collections.singletonList(new DegradeRule("slow").setGrade(RuleConstant.DEGRADE_GRADE_RT).setCount(100).setTimeWindow(5).setSlowRatioThreshold(0)));
            },"rt");
        };
    }
}
