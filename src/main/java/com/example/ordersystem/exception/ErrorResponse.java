package com.example.ordersystem.exception;

import jakarta.validation.constraints.NotNull;

public class ErrorResponse {
    @NotNull
    public String error;

    public ErrorResponse(String error) {
        this.error = error;
    }
}
