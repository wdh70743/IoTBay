package org.example.iotbay.service;

import org.example.iotbay.dto.OrderDTO;
import org.example.iotbay.dto.OrderDTO.Request;
import org.example.iotbay.dto.OrderDTO.Response;

import java.util.List;

public interface OrderService {
    Response createOrder(Request request);
    Response getOrderById(Long orderId);
    List<Response> getOrdersByUserId(Long userId);
    Response updateOrder(Long orderId, Request request);
    String deleteOrder(Long orderId);

}
