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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
class OrderServiceImplTest {
    private static final int DISTANCE = 100;

    @InjectMocks
    private final OrderServiceImpl orderService = new OrderServiceImpl();

    @Mock
    GoogleMapAPIService googleMapAPIService;

    @Mock
    OrderRepository orderRepository;

    @Test
    void create() {
        var request = new CreateOrderRequest(
                List.of("22.332389636564297", "114.16876492337344"),
                List.of("22.336273035980003", "114.17647984732214")
        );

        Mockito.when(googleMapAPIService.getDistance(any(), any(), any(), any())).thenReturn(Optional.of(DISTANCE));

        Order order = order(DISTANCE, Order.Status.UNASSIGNED);
        Mockito.when(orderRepository.save(any())).thenReturn(order);

        CreateOrderResponse response = orderService.create(request);
        assertEquals(order.getId(), response.getId());
        assertEquals(DISTANCE, response.getDistance());
        assertEquals(OrderStatus.UNASSIGNED, response.getStatus());
    }

    @Test
    void updateOrderStatusSuccess() {
        var request = new UpdateOrderStatusRequest(OrderStatus.TAKEN);

        Mockito.when(orderRepository.findById(any())).thenReturn(optionalOrder(DISTANCE, Order.Status.UNASSIGNED));
        Mockito.when(orderRepository.setOrderStatusById(any(), any(), any())).thenReturn(1);

        UpdateOrderStatusResponse response = orderService.updateOrderStatus("", request);
        assertEquals(UpdateOrderStatusResponse.Status.SUCCESS, response.getStatus());
    }

    @Test
    void updateTakenOrderConcurrently() {
        var request = new UpdateOrderStatusRequest(OrderStatus.TAKEN);

        Mockito.when(orderRepository.findById(any())).thenReturn(optionalOrder(DISTANCE, Order.Status.UNASSIGNED));
        Mockito.when(orderRepository.setOrderStatusById(any(), any(), any())).thenReturn(0);

        assertThrows(OrderAPIException.class, () -> orderService.updateOrderStatus("", request));
    }

    @Test
    void updateUnknownOrder() {
        var request = new UpdateOrderStatusRequest(OrderStatus.TAKEN);

        Mockito.when(orderRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(OrderAPIException.class, () -> orderService.updateOrderStatus("", request));
    }

    @Test
    void updateTakenOrder() {
        var request = new UpdateOrderStatusRequest(OrderStatus.TAKEN);

        Mockito.when(orderRepository.findById(any())).thenReturn(optionalOrder(DISTANCE, Order.Status.TAKEN));

        assertThrows(OrderAPIException.class, () -> orderService.updateOrderStatus("", request));
    }

    @Test
    void listOrders() {
        int page = 1;
        int limit = 10;

        Mockito.when(orderRepository.findAll(anyInt(), anyInt())).thenReturn(listResult(limit));

        List<GetOrderResponse> response = orderService.listOrders(page, limit);
        assertEquals(limit, response.size());
    }

    @Test
    void parseAndValidateCoordinate() {
        var normalRequest = new CreateOrderRequest(
                List.of("22.332389636564297", "114.16876492337344"),
                List.of("22.336273035980003", "114.17647984732214")
        );

        Optional<OrderServiceImpl.Coordinate> actual = orderService.parseAndValidateCoordinate(normalRequest);
        assertThat(actual).isNotEmpty();

        OrderServiceImpl.Coordinate expected = expectedCoordinate();

        assertThat(expected).usingRecursiveComparison().isEqualTo(actual.get());

        var outOfRangeRequest = new CreateOrderRequest(
                List.of("-90.1", "114.16876492337344"),
                List.of("22.336273035980003", "114.17647984732214")
        );

        assertThat(orderService.parseAndValidateCoordinate(outOfRangeRequest)).isEmpty();

        var nonNumericalStringRequest = new CreateOrderRequest(
                List.of("abc", "abc"),
                List.of("xyz", "xyz")
        );

        assertThat(orderService.parseAndValidateCoordinate(nonNumericalStringRequest)).isEmpty();
    }

    private Order order(int distance, Order.Status status) {
        var order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setOriginLatitude("22.332389636564297");
        order.setOriginLongitude("114.16876492337344");
        order.setDestinationLatitude("22.336273035980003");
        order.setDestinationLongitude("114.17647984732214");
        order.setDistance(distance);
        order.setStatus(status);
        order.setCreatedTime(ZonedDateTime.now());

        return order;
    }

    private Optional<Order> optionalOrder(int distance, Order.Status status) {
        Order order = order(distance, status);

        return Optional.of(order);
    }

    private List<Order> listResult(int n) {
        List<Order> orders = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            orders.add(order(DISTANCE, Order.Status.UNASSIGNED));
        }

        return orders;
    }

    private OrderServiceImpl.Coordinate expectedCoordinate() {
        return new OrderServiceImpl.Coordinate(
                22.332389636564297, 114.16876492337344,
                22.336273035980003, 114.17647984732214
        );
    }
}
