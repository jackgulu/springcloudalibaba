package com.hikvision.api;

import java.util.List;

import com.hikvision.entity.UserAddress;

public interface OrderService {

    /**
     * 初始化订单
     * @param userId
     */
    public List<UserAddress> initOrder(String userId);

}
