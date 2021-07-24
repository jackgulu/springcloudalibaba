package com.hikvision.rule;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.hikvision.context.RibbonRequestContext;
import com.hikvision.context.RibbonRequestContextHolder;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrayRule extends AbstractLoadBalancerRule {
    private Random random = new Random();
    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object key) {
        boolean grayInvocation = false;
        try {
            String grayTag = RibbonRequestContextHolder.getCurrentContext().get("Gray");
            if (!StringUtils.isEmpty(grayTag) && grayTag.equals(Boolean.TRUE.toString())) {
                grayInvocation = true;
            }
            List<Server> reachableServers = this.getLoadBalancer().getReachableServers();
            List<Server> grayServerList = new ArrayList<>();
            List<Server> notmalServerList = new ArrayList<>();
            for (Server server : reachableServers) {
                NacosServer nacosServer = (NacosServer) server;
                if (nacosServer.getMetadata().containsKey("gray") && nacosServer.getMetadata().get("gray").equals("true")) {
                    grayServerList.add(server);
                } else {
                    notmalServerList.add(server);
                }
            }
            if (grayInvocation) {
                return grayServerList.get(random.nextInt(grayServerList.size()));
            } else {
                return notmalServerList.get(random.nextInt(notmalServerList.size()));
            }
        }finally {
            RibbonRequestContextHolder.clearContext();
        }
    }
}
