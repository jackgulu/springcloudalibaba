package com.hikvision.service.impl;

import com.hikvision.service.LoopLongPollingService;
import com.hikvision.task.lock.LockPullTask;
import com.hikvision.task.lock.LockPushTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
@Service
public class LockLongPollingServiceImpl implements LoopLongPollingService {

    @Autowired
    ScheduledExecutorService scheduler;
    private LockPullTask lockPullTask;
    private Object lock;

    @PostConstruct
    public void post() {
        lock = new Object();
    }

    private String dealTimeOut() {
        synchronized (lock) {
            lock.notifyAll();
            lockPullTask.setData("timeout");
        }
        return "timeout";
    }


    @Override
    public String pull() {
        lockPullTask = new LockPullTask(lock);
        Future<String> future = scheduler.submit(lockPullTask);
        try {
            return future.get(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            return dealTimeOut();
        }
        return "ex";

    }

    @Override
    public String push(String data) {
        Future<String> future = scheduler.schedule(new LockPushTask(lockPullTask, data, lock), 0L,
                TimeUnit.MILLISECONDS);
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
