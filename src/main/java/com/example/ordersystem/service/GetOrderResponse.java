package com.example.ordersystem.service;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOrderResponse {
    @NotNull
    private String id;

    @NotNull
    private Integer distance;

    @NotNull
    private OrderStatus status;

    public GetOrderResponse(String id, Integer distance, OrderStatus status) {
        this.id = id;
        this.distance = distance;
        this.status = status;
    }
}
