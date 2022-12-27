package com.example.ordersystem.service;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    @NotNull
    private OrderStatus status;

    public UpdateOrderStatusRequest() {

    }

    public UpdateOrderStatusRequest(OrderStatus status) {
        this.status = status;
    }
}
