package com.hikvision.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.reactive.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
public class RandomLoadBalancer implements ReactorServiceInstanceLoadBalancer {
private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers;
private final String serviceId;
private final Random random;
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        System.out.println("我开始选择了");
        ServiceInstanceListSupplier supplier = serviceInstanceListSuppliers.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get().next().map(this::getInstanceResponse);
    }

    public RandomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSuppliers,
    String serviceId){
        this.serviceId=serviceId;
        this.random=new Random();
        this.serviceInstanceListSuppliers=serviceInstanceListSuppliers;
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances){
        if(instances.isEmpty()){
            return new EmptyResponse();
        }
        ServiceInstance serviceInstance = instances.get(random.nextInt(instances.size()));
        return new DefaultResponse(serviceInstance);
    }

}
