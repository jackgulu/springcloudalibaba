package com.hikvision.api;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders(String userId);

    Order findOrder(String orderId);

}