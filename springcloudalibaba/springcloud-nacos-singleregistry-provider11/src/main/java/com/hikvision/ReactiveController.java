package com.hikvision;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.client.config.NacosConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
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

    private String serviceName="nacos-provider";

    @Autowired
    private NacosConfigProperties nacosConfigProperties;
    @Autowired
    private ApplicationContext applicationContext;
    @GetMapping("/services")
    public Flux<String> info(){
    return reactiveDiscoveryClient.getServices();
    }
    @GetMapping("/helloTest")
    public String helloTest(){
        return "nacos-provider12:8082";
    }
    @GetMapping("/instances")
    public Flux<String> instance(){
        return reactiveDiscoveryClient.getInstances(serviceName).map(instance->"[serviceId: "+instance.getServiceId()+
                ",host:"+instance.getHost()+
                ",port:"+instance.getPort()+
                ",schema:"+instance.getScheme()+
                ",uri: "+instance.getUri()+"]");
    }
    @RequestMapping("/hello")
    public Mono<String> hello(){
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
        ServiceInstance serviceInstance = instances.stream().findAny().orElseThrow(() -> new IllegalStateException("no " + serviceName + "instance available"));
        return WebClient.create("http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()).get().uri("/echo?name=nacos").retrieve().bodyToMono(String.class);
    }


    @GetMapping("/env")
    public String config(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("env.get('boot.category')="+applicationContext.getEnvironment().getProperty("boot.category","unkonw"));
        return stringBuilder.toString();
    }

    @GetMapping("/getConfig")
    public String getConfig(){
        ConfigService configService = nacosConfigProperties.configServiceInstance();
        return configService.toString();
    }
}
