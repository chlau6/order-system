package com.example.ordersystem.service.impl;

import com.example.ordersystem.domain.Order;
import com.example.ordersystem.exception.OrderAPIException;
import com.example.ordersystem.google.GoogleMapAPIService;
import com.example.ordersystem.service.CreateOrderRequest;
import com.example.ordersystem.service.CreateOrderResponse;
import com.example.ordersystem.service.GetOrderResponse;
import com.example.ordersystem.service.OrderStatus;
import com.example.ordersystem.service.UpdateOrderStatusRequest;
import com.example.ordersystem.service.UpdateOrderStatusResponse;
import com.example.ordersystem.repository.OrderRepository;
import com.example.ordersystem.service.OrderService;
import com.example.ordersystem.util.MessageFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private GoogleMapAPIService googleMapAPIService;

    @Override
    public CreateOrderResponse create(CreateOrderRequest request) {
        Coordinate coordinate = parseAndValidateCoordinate(request).orElseThrow(() -> new OrderAPIException(HttpStatus.BAD_REQUEST, "Invalid Coordinate"));

        Integer distance = googleMapAPIService.getDistance(
                coordinate.originLatitude, coordinate.originLongitude,
                coordinate.destinationLatitude, coordinate.destinationLongitude
        ).orElseThrow(() -> new OrderAPIException(HttpStatus.BAD_REQUEST, "Route Not Found"));

        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOriginLatitude(request.getOriginLatitude());
        order.setOriginLongitude(request.getOriginLongitude());
        order.setDestinationLatitude(request.getDestinationLatitude());
        order.setDestinationLongitude(request.getOriginLongitude());
        order.setDistance(distance);
        order.setStatus(Order.Status.UNASSIGNED);
        order.setCreatedTime(ZonedDateTime.now());

        order = orderRepository.save(order);

        return new CreateOrderResponse(order.getId(), order.getDistance(), OrderStatus.UNASSIGNED);
    }

    @Override
    public UpdateOrderStatusResponse updateOrderStatus(String id, UpdateOrderStatusRequest request) {
        Order.Status status = Order.Status.valueOf(request.getStatus());

        Order order = orderRepository.findById(id).orElseThrow(
                () -> new OrderAPIException(HttpStatus.NOT_FOUND, MessageFormatter.format("Order not found, id=%s", id)));

        if (order.getStatus() == status) {
            throw new OrderAPIException(HttpStatus.BAD_REQUEST, MessageFormatter.format("Order has been already %s, id=%s", status, id));
        }

        int rowsChanged = orderRepository.setOrderStatusById(
                id, order.getStatus().name(), status.name());

        if (rowsChanged != 1) {
            throw new OrderAPIException(HttpStatus.CONFLICT, "Take order failed, orderId=" + id);
        }

        return new UpdateOrderStatusResponse(UpdateOrderStatusResponse.Status.SUCCESS);
    }

    @Override
    public List<GetOrderResponse> listOrders(Integer page, Integer limit) {
        List<Order> orders = orderRepository.findAll((page - 1) * limit, limit);

        List<GetOrderResponse> response = new ArrayList<>();
        for (Order order : orders) {
            response.add(new GetOrderResponse(order.getId(), order.getDistance(), OrderStatus.valueOf(order.getStatus().name())));
        }

        return response;
    }

    Optional<Coordinate> parseAndValidateCoordinate(CreateOrderRequest createOrderRequest) {
        double originLatitude, originLongitude, destinationLatitude, destinationLongitude;
        try {
            originLatitude = Double.parseDouble(createOrderRequest.getOriginLatitude());
            originLongitude = Double.parseDouble(createOrderRequest.getOriginLongitude());
            destinationLatitude = Double.parseDouble(createOrderRequest.getDestinationLatitude());
            destinationLongitude = Double.parseDouble(createOrderRequest.getDestinationLongitude());
        } catch (NumberFormatException e) {
            return Optional.empty();
        }

        if (originLatitude < -90 || originLatitude > 90
                || originLongitude < -180 || originLongitude > 180
                || destinationLatitude < -90 || destinationLatitude > 90
                || destinationLongitude < -180 || destinationLongitude > 180) {
            return Optional.empty();
        }

        var coordinate = new Coordinate(originLatitude, originLongitude, destinationLatitude, destinationLongitude);
        return Optional.of(coordinate);
    }

    static class Coordinate {
        private final Double originLatitude, originLongitude, destinationLatitude, destinationLongitude;

        public Coordinate(double originLatitude, double originLongitude, double destinationLatitude, double destinationLongitude) {
            this.originLatitude = originLatitude;
            this.originLongitude = originLongitude;
            this.destinationLatitude = destinationLatitude;
            this.destinationLongitude = destinationLongitude;
        }
    }
}
