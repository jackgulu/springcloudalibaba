package com.hikvision.task.common;

import java.util.concurrent.Callable;

public class LoopPushTask implements Callable<String> {
    private LoopPullTask loopPullTask;
    private String data;

    public LoopPushTask(LoopPullTask loopPullTask, String data) {
        this.loopPullTask = loopPullTask;
        this.data = data;
    }

    @Override
    public String call() throws Exception {
        loopPullTask.setData(data);
        return "changed";
    }

}
