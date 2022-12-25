package com.example.ordersystem.exception;

import org.springframework.http.HttpStatusCode;

public class OrderAPIException extends RuntimeException {
    public HttpStatusCode httpStatusCode;
    public String message;

    public OrderAPIException(HttpStatusCode httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }
}
