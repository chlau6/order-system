package com.example.ordersystem.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusResponse {
    @NotNull
    public UpdateOrderStatusResponse.Status status;

    public UpdateOrderStatusResponse(Status status) {
        this.status = status;
    }

    public enum Status {
        SUCCESS
    }
}
