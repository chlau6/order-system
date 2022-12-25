package com.example.ordersystem.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateOrderRequest {
    @NotNull
    @Size(min = 2, max = 2, message = "origin size must be 2")
    public List<String> origin;

    @NotNull
    @Size(min = 2, max = 2, message = "destination size must be 2")
    public List<String> destination;

    public CreateOrderRequest(List<String> origin, List<String> destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public String getOriginLatitude() {
        return origin.get(0);
    }

    public String getOriginLongitude() {
        return origin.get(1);
    }

    public String getDestinationLatitude() {
        return destination.get(0);
    }

    public String getDestinationLongitude() {
        return destination.get(1);
    }
}
