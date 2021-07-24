package com.hikvision;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.ServiceInstanceChooser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
@Component
public class RandomServiceInstanceChooser implements ServiceInstanceChooser {
    private final Random random;

    private final DiscoveryClient discoveryClient;
    public RandomServiceInstanceChooser(DiscoveryClient discoveryClient) {
        random = new Random();
        this.discoveryClient=discoveryClient;
    }

    @Override
    public ServiceInstance choose(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        return instances.get(random.nextInt(instances.size()));
    }
}
