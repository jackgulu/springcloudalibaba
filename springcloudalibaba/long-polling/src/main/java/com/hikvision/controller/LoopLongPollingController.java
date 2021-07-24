package com.hikvision.controller;

import com.hikvision.dto.Result;
import com.hikvision.service.LoopLongPollingService;
import com.hikvision.service.impl.LockLongPollingServiceImpl;
import com.hikvision.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loop")
public class LoopLongPollingController {


    @Autowired
    LockLongPollingServiceImpl loopLongPollingService;
    @Autowired
    LockLongPollingServiceImpl lockLongPollingService;
    /**
     * 从服务端拉取被变更的数据
     *
     * @return
     */
    @GetMapping("/pull")
    public Result pull() {
        String result = loopLongPollingService.pull();
        return ResultUtil.success(result);
    }
    @GetMapping("/pullLock")
    public Result pullLock() {
        String result = lockLongPollingService.pull();
        return ResultUtil.success(result);
    }
    /**
     * 向服务端推送变更的数据
     *
     * @param data
     * @return
     */
    @GetMapping("/push")
    public Result push(@RequestParam("data") String data) {
        String result = lockLongPollingService.push(data);
        return ResultUtil.success(result);
    }

    @GetMapping("/pushLock")
    public Result pushLock(@RequestParam("data") String data) {
        String result = loopLongPollingService.push(data);
        return ResultUtil.success(result);
    }
}
