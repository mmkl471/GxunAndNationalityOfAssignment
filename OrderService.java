package com.example.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.demo.entity.Order;

import java.util.List;

public interface OrderService extends IService<Order> {
    List<Order> getOrdersByUserId(Long userId);
}
