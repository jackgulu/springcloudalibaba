package com.hikvision.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface NacosLongPollingService {
    void doGet(String dataId, HttpServletRequest req, HttpServletResponse resp);
    void push(String dataId, String data);
}
