package com.hikvision;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.hikvision.com.hikvision.feignapi.EchoService;
import com.hikvision.inteceptor.GrayInterceptor;
import com.hikvision.inteceptor.GrayRequestInterceptor;
import com.hikvision.rule.GrayRule;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient(autoRegister = false)
@RestController
@RibbonClients(defaultConfiguration = {GrayRule.class})
public class NacosConsumerApplication {
    @Autowired
    private RestTemplate template;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private EchoService echoService;

    @Autowired
    private RestTemplate restTemplate;
   @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;
    private String serviceName="nacos-provider";
    @GetMapping("/testLoadBalance")
    public String loadBalanceTest(){
        ServiceInstance choose = loadBalancerClient.choose(serviceName);
        return template.getForObject("http://"+choose.getHost()+":"+choose.getPort()+"/reactive/helloTest",String.class);

    }

 /*   @GetMapping("/testRibbion")
    public String testRibbion(){
        String group = nacosDiscoveryProperties.getGroup();
        return template.getForObject("http://"+serviceName+"/reactive/helloTest",String.class);
    }
*/
    @GetMapping("/testFeign")
    public String testFeign(HttpServletRequest request){
        return echoService.echo();
    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate=new RestTemplate();
        restTemplate.getInterceptors().add(new GrayInterceptor());
        return restTemplate;
    }
    @Bean
    public GrayRequestInterceptor requestInterceptor() {
        return new GrayRequestInterceptor();
    }
    @GetMapping("/testGray")
    public ResponseEntity<String> testGray(HttpServletRequest request){
        HttpHeaders headers = new HttpHeaders();
        /*if (StringUtils.isNotEmpty(request.getHeader("Gray"))) {
            headers.add("Gray", request.getHeader("Gray").equals("true") ? "true" : "false");
        }*/
        headers.add("Gray","true");
        HttpEntity httpEntity=new HttpEntity(headers);
        return restTemplate.exchange("http://nacos-provider/reactive/helloTest", HttpMethod.GET,httpEntity,String.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(NacosConsumerApplication.class,args);
    }
}
