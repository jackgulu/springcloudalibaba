package com.hikvision.service.impl;

import com.hikvision.service.LoopLongPollingService;
import com.hikvision.task.common.LoopPullTask;
import com.hikvision.task.common.LoopPushTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class LoopLongPollingServiceImpl implements LoopLongPollingService {

    @Autowired
    ScheduledExecutorService scheduler;

    private LoopPullTask loopPullTask;

    @Override
    public String pull() {
        loopPullTask = new LoopPullTask();
        scheduler.schedule(loopPullTask, 0L, TimeUnit.MILLISECONDS);
        try {
            return loopPullTask.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ex";

    }

    @Override
    public String push(String data) {
        Future<String> future = scheduler.schedule(new LoopPushTask(loopPullTask, data), 0L, TimeUnit.MILLISECONDS);
        try {
            return future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "ex";
    }
}
