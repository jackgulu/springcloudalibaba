package com.hikvision.command;


import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.web.client.RestTemplate;

public class HelloWorldCommand extends HystrixCommand<String> {

    private  final RestTemplate restTemplate = new RestTemplate();
    private final String code;

    public HelloWorldCommand(String code){
        super(HystrixCommandGroupKey.Factory.asKey("HelloWorldExample"));
        this.code=code;
    }
    @Override
    protected String run() throws Exception {
        String url = "http://httpbin.org/status/" + code;
        System.out.println("start to curl: " + url);
        restTemplate.getForObject(url,String.class);
        return "Request Success";
    }

    @Override
    protected String getFallback() {
        return "Request failed";
    }
}
