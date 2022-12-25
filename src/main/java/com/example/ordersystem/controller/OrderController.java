package com.example.ordersystem.controller;

import com.example.ordersystem.payload.CreateOrderRequest;
import com.example.ordersystem.payload.CreateOrderResponse;
import com.example.ordersystem.payload.GetOrderResponse;
import com.example.ordersystem.payload.UpdateOrderStatusRequest;
import com.example.ordersystem.payload.UpdateOrderStatusResponse;
import com.example.ordersystem.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Validated
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping(value = "/order")
    public CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest createOrderRequest) {
        return orderService.create(createOrderRequest);
    }

    @PatchMapping(value = "/orders/{id}")
    public UpdateOrderStatusResponse takeOrder(@PathVariable String id, @Valid @RequestBody UpdateOrderStatusRequest request) {
        return orderService.updateOrderStatus(id, request);
    }

    @Valid
    @GetMapping(value = "/orders")
    public List<GetOrderResponse> listOrders(@RequestParam @Min(1) Integer page, @RequestParam @Min(1) Integer limit) {
        return orderService.listOrders(page, limit);
    }
}
