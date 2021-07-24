package com.hikvision.inteceptor;

import com.hikvision.context.RibbonRequestContext;
import com.hikvision.context.RibbonRequestContextHolder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class GrayInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if(request.getHeaders().containsKey("Gray")){
            String value =request.getHeaders().getFirst("Gray");
            if(value.equals("true")){
                RibbonRequestContextHolder.getCurrentContext().put("Gray",Boolean.TRUE.toString());
            }
        }else {
            RibbonRequestContextHolder.getCurrentContext().put("Gray",Boolean.FALSE.toString());
        }
        return execution.execute(request,body);
    }
}
