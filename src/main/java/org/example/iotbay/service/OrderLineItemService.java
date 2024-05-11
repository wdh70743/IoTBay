package org.example.iotbay.service;


import org.example.iotbay.dto.OrderDTO;

import java.util.List;

public interface OrderLineItemService {
    List<OrderDTO.OrderLineItemDTO> createOrderLineItems(Long orderId, List<OrderDTO.OrderLineItemDTO> items);
    List<OrderDTO.OrderLineItemDTO> getOrderLineItemsByOrderId(Long orderId);
    OrderDTO.OrderLineItemDTO updateOrderItem(Long orderLineItemId, OrderDTO.OrderLineItemDTO item);
    String deleteOrderItem(Long orderItemId);
}
