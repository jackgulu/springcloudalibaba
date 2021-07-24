package com.hikvision.service.impl;

import com.hikvision.service.NacosLongPollingService;
import com.hikvision.task.nacos.NacosPullTask;
import com.hikvision.task.nacos.NacosPushTask;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
@Service
public class NacosLongPollingServiceImpl implements NacosLongPollingService {
    final ScheduledExecutorService scheduler;
    final Queue<NacosPullTask> nacosPullTasks;

    public NacosLongPollingServiceImpl(){
        scheduler=new ScheduledThreadPoolExecutor(1,r -> {
            Thread t=new Thread(r);
            t.setName("NacosLongPollingTask");
            t.setDaemon(true);
            return t;
        });
        nacosPullTasks=new ConcurrentLinkedQueue<>();
        scheduler.scheduleAtFixedRate(() -> System.out.println("线程存活状态:" + new Date()), 0L, 60, TimeUnit.SECONDS);
    }
    @Override
    public void doGet(String dataId, HttpServletRequest req, HttpServletResponse resp) {
        final AsyncContext asyncContext = req.startAsync();
        scheduler.execute(new NacosPullTask(nacosPullTasks, scheduler, asyncContext, dataId, req, resp));
    }

    @Override
    public void push(String dataId, String data) {
        scheduler.schedule(new NacosPushTask(dataId, data, nacosPullTasks), 0L, TimeUnit.MILLISECONDS);
    }
}
