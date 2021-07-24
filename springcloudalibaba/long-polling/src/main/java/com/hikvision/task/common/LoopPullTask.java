package com.hikvision.task.common;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.Callable;

@Slf4j
public class LoopPullTask implements Callable<String> {
    @Getter
    @Setter
    public volatile String data;
    private Long TIME_OUT_MILLIS = 10000L;

    private boolean isTimeOut(Long startTime) {
        Long nowTime = System.currentTimeMillis();
        return nowTime - startTime > TIME_OUT_MILLIS;
    }

    @Override
    public String call() throws Exception {
        Long startTime = System.currentTimeMillis();
        while (true) {
            if (!StringUtils.isEmpty(data)) {
                System.out.println("当前的数据为"+data);
                return data;
            }
            if(isTimeOut(startTime)){
                log.info("获取数据请求超时" + new Date());
                data = "请求超时";
                return data;
            }
            //减轻CPU压力
            Thread.sleep(200);
        }
    }
}
