package com.hikvision.inteceptor;

import com.hikvision.context.RibbonRequestContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class GrayRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        if(requestTemplate.headers().containsKey("Gray")){
            String gray = requestTemplate.headers().get("Gray").iterator().next();
            if(gray.equals("true")){
                RibbonRequestContextHolder.getCurrentContext().put("Gray",Boolean.TRUE.toString());
            }
        }
    }
}
