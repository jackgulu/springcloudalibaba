package com.hikvision.task.nacos;

import com.hikvision.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class NacosPullTask implements Runnable {
    Queue<NacosPullTask> nacosPullTasks;
    ScheduledExecutorService scheduler;
    AsyncContext asyncContext;
    String dataId;
    HttpServletRequest req;
    HttpServletResponse resp;
    Future<?> asyncTimeoutFuture;

    public NacosPullTask(Queue<NacosPullTask> nacosPullTasks, ScheduledExecutorService scheduler,
                         AsyncContext asyncContext, String dataId, HttpServletRequest req, HttpServletResponse resp) {
        this.nacosPullTasks = nacosPullTasks;
        this.scheduler = scheduler;
        this.asyncContext = asyncContext;
        this.dataId = dataId;
        this.req = req;
        this.resp = resp;
    }

    @Override
    public void run() {
        scheduler.schedule(() -> {
            log.info("10秒后开始执行长轮询任务:" + new Date());
            nacosPullTasks.remove(NacosPullTask.this);
        }, 10, TimeUnit.SECONDS);
        nacosPullTasks.add(this);
    }

    public void sendResponse(String result) {
        System.out.println("发送响应:" + new Date());
        if (asyncTimeoutFuture != null) {
            asyncTimeoutFuture.cancel(false);
        }
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache,no-store");
        resp.setDateHeader("Expires", 0);
        resp.setStatus(HttpServletResponse.SC_OK);
        sendJsonResult(result);
    }

    private void sendJsonResult(String result) {
        Result<String> pojoResult = new Result<>();
        pojoResult.setCode(200);
        pojoResult.setSuccess(!StringUtils.isEmpty(result));
        pojoResult.setData(result);
        PrintWriter writer = null;
        try {
            writer = asyncContext.getResponse().getWriter();
            writer.write(pojoResult.toString());
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            asyncContext.complete();
            if (null != writer) {
                writer.close();
            }
        }
    }

}
