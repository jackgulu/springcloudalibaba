package com.hikvision.api;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.UUID;

public class Order implements Serializable {

    private String id;

    private Timestamp createdTime;

    private String userId;

    public static Order generate(String userId) {
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        order.setUserId(userId);
        return order;
    }

    public static Order error() {
        Order order = new Order();
        order.setId("-1");
        order.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        order.setUserId("none");
        return order;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}