package com.example.ordersystem.payload;

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
