package com.hikvision.config;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DataSourceInitFunc implements InitFunc {
    @Override
    public void init() throws Exception {
        String remoteAddress="localhost";
        String groupId="Sentinel:Demo";
        String dataId="com.alibaba.csp.sentinel.demo.flow.rule";
        ReadableDataSource<String, List<FlowRule>>
                flowRuleDataSource=new NacosDataSource(remoteAddress,groupId,dataId,source->
                   JSON.parseObject((String) source,new TypeReference<List<FlowRule>>(){}.getType())
                );
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
