package com.example.ordersystem.service;

import com.example.ordersystem.payload.CreateOrderRequest;
import com.example.ordersystem.payload.CreateOrderResponse;
import com.example.ordersystem.payload.GetOrderResponse;
import com.example.ordersystem.payload.UpdateOrderStatusRequest;
import com.example.ordersystem.payload.UpdateOrderStatusResponse;

import java.util.List;

public interface OrderService {
    CreateOrderResponse create(CreateOrderRequest request);

    UpdateOrderStatusResponse updateOrderStatus(String id, UpdateOrderStatusRequest request);

    List<GetOrderResponse> listOrders(Integer page, Integer limit);
}
