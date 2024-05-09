package org.example.iotbay.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.Order;
import org.example.iotbay.domain.OrderLineItem;
import org.example.iotbay.domain.Product;
import org.example.iotbay.domain.User;
import org.example.iotbay.dto.OrderDTO;
import org.example.iotbay.repository.OrderLineItemRepository;
import org.example.iotbay.repository.OrderRepository;
import org.example.iotbay.repository.ProductRepository;
import org.example.iotbay.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final ProductRepository productRepository;
    private final OrderLineItemService orderLineItemService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public OrderDTO.Response createOrder(OrderDTO.Request request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(request.getStatus());

        // Save the order first to ensure it has an ID
        Order savedOrder = orderRepository.save(order);

        // Now handle the order line items
        List<OrderLineItem> orderItems = request.getItems().stream()
                .map(itemDto -> {
                    OrderLineItem orderItem = new OrderLineItem();
                    orderItem.setOrder(savedOrder); // Ensure order is set with savedOrder which has ID
                    orderItem.setProduct(productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId())));
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setPrice(itemDto.getPrice());
                    return orderItem;
                }).collect(Collectors.toList());

        // Save all the order items at once
        orderLineItemRepository.saveAll(orderItems);

        // Optionally, update the order with the items if needed
        savedOrder.setItems(new HashSet<>(orderItems));
        orderRepository.save(savedOrder);

        return modelMapper.map(savedOrder, OrderDTO.Response.class);
    }


    @Override
    public OrderDTO.Response getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        return modelMapper.map(order, OrderDTO.Response.class);
    }

    @Override
    public List<OrderDTO.Response> getOrdersByUserId(Long userId) {

        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.Response.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO.Response updateOrder(Long orderId, OrderDTO.Request request) {
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        modelMapper.map(request, existingOrder);

        if (!existingOrder.getUser().getId().equals(request.getUserId())) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + request.getUserId()));
            existingOrder.setUser(user);
        }
        Order updatedOrder = orderRepository.save(existingOrder);

        // If you want to replace existing items, you may need to clear and re-add them
        orderLineItemService.deleteOrderItem(orderId);
        List<OrderDTO.OrderLineItemDTO> itemsDto = orderLineItemService.createOrderLineItems(updatedOrder.getId(), request.getItems());
        Set<OrderLineItem> items = itemsDto.stream().map(itemDto -> {
            OrderLineItem orderLineItem = modelMapper.map(itemDto, OrderLineItem.class);
            orderLineItem.setOrder(updatedOrder); // Set the updatedOrder reference here
            return orderLineItem;
        }).collect(Collectors.toSet()); // Collect into a Set

        updatedOrder.setItems(items);
        orderRepository.save(updatedOrder); // Save the updated order with its items

        return modelMapper.map(updatedOrder, OrderDTO.Response.class);
    }

    @Override
    public String deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        orderRepository.delete(order);
        return "Order successfully deleted";
    }
}
