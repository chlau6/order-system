package com.example.ordersystem.service;

import java.util.List;

public interface OrderService {
    CreateOrderResponse create(CreateOrderRequest request);

    UpdateOrderStatusResponse updateOrderStatus(String id, UpdateOrderStatusRequest request);

    List<GetOrderResponse> listOrders(Integer page, Integer limit);
}
