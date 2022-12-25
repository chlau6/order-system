package com.example.ordersystem.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrderResponse {
    @NotNull
    private String id;

    @NotNull
    private Integer distance;

    @NotNull
    private OrderStatus status;

    public CreateOrderResponse(String id, Integer distance, OrderStatus orderStatus) {
        this.id = id;
        this.distance = distance;
        this.status = orderStatus;
    }
}
