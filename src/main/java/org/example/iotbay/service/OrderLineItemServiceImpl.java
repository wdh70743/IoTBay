package org.example.iotbay.service;

import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.Order;
import org.example.iotbay.domain.OrderLineItem;

import org.example.iotbay.domain.Product;
import org.example.iotbay.dto.OrderDTO;
import org.example.iotbay.repository.OrderLineItemRepository;
import org.example.iotbay.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderLineItemServiceImpl implements OrderLineItemService {
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    private static final Logger log = LoggerFactory.getLogger(OrderLineItemServiceImpl.class);
    @Override
    public List<OrderDTO.OrderLineItemDTO> createOrderLineItems(Long orderId, List<OrderDTO.OrderLineItemDTO> itemsDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        List<OrderLineItem> items = itemsDto.stream()
                .map(dto -> {
                    OrderLineItem item = modelMapper.map(dto, OrderLineItem.class);
                    item.setOrder(order);
                    return item;
                })
                .collect(Collectors.toList());

        orderLineItemRepository.saveAll(items);

        return items.stream()
                .map(item -> modelMapper.map(item, OrderDTO.OrderLineItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO.OrderLineItemDTO> getOrderLineItemsByOrderId(Long orderId) {
        List<OrderLineItem> items = orderLineItemRepository.findByOrderId(orderId);
        return items.stream()
                .map(item -> modelMapper.map(item, OrderDTO.OrderLineItemDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO.OrderLineItemDTO updateOrderItem(Long orderLineItemId, OrderDTO.OrderLineItemDTO itemDto) {
        OrderLineItem item = orderLineItemRepository.findById(orderLineItemId)
                .orElseThrow(() -> new RuntimeException("Order item not found with ID: " + orderLineItemId));
        modelMapper.map(itemDto, item);
        item = orderLineItemRepository.save(item);
        return modelMapper.map(item, OrderDTO.OrderLineItemDTO.class);
    }


    @Override
    public String deleteOrderItem(Long orderItemId) {
        orderLineItemRepository.deleteById(orderItemId);
        return "Order item successfully deleted";
    }
}
