package com.hikvision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.endpoint.event.RefreshEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/reactive")
public class ReactiveController {
    @Autowired
    private ReactiveDiscoveryClient reactiveDiscoveryClient;
    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private ApplicationContext applicationContext;

    private String serviceName="nacos-provider";
    @GetMapping("/services")
    public Flux<String> info(){
    return reactiveDiscoveryClient.getServices();
    }
    @GetMapping("/instances")
    public Flux<String> instance(){
        return reactiveDiscoveryClient.getInstances(serviceName).map(instance->"[serviceId: "+instance.getServiceId()+
                ",host:"+instance.getHost()+
                ",port:"+instance.getPort()+
                ",schema:"+instance.getScheme()+
                ",uri: "+instance.getUri()+"]");
    }
    @GetMapping("/helloTest")
    public String helloTest(){
        return "nacos-provider13:8083";
    }
    @RequestMapping("/hello")
    public Mono<String> hello(){
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        ServiceInstance serviceInstance = instances.stream().findAny().orElseThrow(() -> new IllegalStateException("no " + serviceName + "instance available"));
        return WebClient.create("http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()).get().uri("/echo?name=nacos").retrieve().bodyToMono(String.class);
    }


    @GetMapping("/config")
    public String Config(){
        StringBuilder sb=new StringBuilder();
        sb.append("env.get('book.category')="+applicationContext.getEnvironment().getProperty("book.category1","unknow"))
                .append("<br/>env.get('book.author')="+applicationContext.getEnvironment().getProperty("book.author","unknow"));
        return sb.toString();
    }

    @GetMapping("/event")
    public String event(){
        applicationContext.publishEvent(new RefreshEvent(this, null, "just for test"));
        return "send RefreshEvent";
    }
}
