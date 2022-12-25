package com.example.ordersystem.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    @NotBlank
    private String status;

    public UpdateOrderStatusRequest() {

    }

    public UpdateOrderStatusRequest(String status) {
        this.status = status;
    }
}
