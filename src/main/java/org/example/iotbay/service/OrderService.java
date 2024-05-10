package org.example.iotbay.service;

import org.example.iotbay.domain.Order;
import org.example.iotbay.dto.OrderDTO;
import org.example.iotbay.dto.OrderDTO.Request;
import org.example.iotbay.dto.OrderDTO.Response;

import java.util.List;

public interface OrderService {
    Response createOrder(Request request);

    Order getOrderById(Long orderId);
    List<Order> getOrdersByUserId(Long userId);
    Response updateOrder(Long orderId, Request request);
    String deleteOrder(Long orderId);

}
