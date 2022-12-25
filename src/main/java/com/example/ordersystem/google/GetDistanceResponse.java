package com.example.ordersystem.google;

import java.util.ArrayList;
import java.util.List;

public class GetDistanceResponse {
    public List<Route> routes = new ArrayList<>();

    public static class Route {
        public Integer distanceMeters;
    }
}
