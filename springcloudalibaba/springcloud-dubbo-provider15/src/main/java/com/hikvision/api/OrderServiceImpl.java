package com.hikvision.api;

import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service(version = "1.0.0")
@RestController
public class OrderServiceImpl implements OrderService {

    private static List<Order> orderList = new ArrayList<>();

    static {
        orderList.add(Order.generate("jim"));
        orderList.add(Order.generate("jim"));
        orderList.add(Order.generate("test"));
    }

    @GetMapping("/allOrders")
    @Override
    public List<Order> getAllOrders(@RequestParam("userId") final String userId) {
        return orderList.stream().filter(
                order -> order.getUserId().equals(userId)
        ).collect(Collectors.toList());
    }

    @GetMapping("/findOrder")
    @Override
    public Order findOrder(@RequestParam("orderId") String orderId) {
        return orderList.stream().filter(
                order -> order.getId().equals(orderId)
        ).findFirst().orElseGet(Order::error);
    }

}