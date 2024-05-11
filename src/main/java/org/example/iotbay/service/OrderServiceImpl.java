package org.example.iotbay.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.Order;
import org.example.iotbay.domain.OrderLineItem;
import org.example.iotbay.domain.Product;
import org.example.iotbay.domain.User;
import org.example.iotbay.dto.OrderDTO;
import org.example.iotbay.exception.ResourceNotFoundException;
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

        Order savedOrder = orderRepository.save(order);
        System.out.println("Order created for UserId: " + user.getId());
        List<OrderLineItem> orderItems = request.getItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId()));
                    OrderLineItem orderItem = new OrderLineItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setPrice(itemDto.getPrice());
                    return orderItem;
                }).collect(Collectors.toList());

        orderLineItemRepository.saveAll(orderItems);

        List<OrderDTO.OrderLineItemDTO> itemsDto = orderItems.stream().map(item -> {
            OrderDTO.OrderLineItemDTO dto = new OrderDTO.OrderLineItemDTO();
            dto.setProductId(item.getProduct().getId()); // Manually set productId
            dto.setQuantity(item.getQuantity());
            dto.setPrice(item.getPrice());
            return dto;
        }).collect(Collectors.toList());
        savedOrder.setItems(new HashSet<>(orderItems));
        orderRepository.save(savedOrder);

        OrderDTO.Response response = modelMapper.map(savedOrder, OrderDTO.Response.class);
        response.setItems(itemsDto);
        response.setUserId(savedOrder.getUser().getId());
        return response;
    }


    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {

        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders;
    }

    @Override
    @Transactional
    public OrderDTO.Response updateOrder(Long orderId, OrderDTO.Request request) {

        // Find the existing order
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        // Update order details
        if (!existingOrder.getUser().getId().equals(request.getUserId())) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
            existingOrder.setUser(user);
        }
        existingOrder.setStatus(request.getStatus());

        // Update items
        updateOrderItems(existingOrder, request.getItems());

        // Save the updated order
        orderRepository.save(existingOrder);

        // Convert the updated order to DTO and return
        return modelMapper.map(existingOrder, OrderDTO.Response.class);
    }

    private void updateOrderItems(Order existingOrder, List<OrderDTO.OrderLineItemDTO> itemsDto) {
        // Clear existing items to avoid issues with orphan removal
        existingOrder.getItems().clear();

        // Re-add updated or new items
        for (OrderDTO.OrderLineItemDTO itemDto : itemsDto) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemDto.getProductId()));
            OrderLineItem orderItem = new OrderLineItem();
            orderItem.setOrder(existingOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setPrice(itemDto.getPrice());
            existingOrder.getItems().add(orderItem);
        }
    }


    @Override
    public String deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        orderRepository.delete(order);
        return "Order successfully deleted";
    }
}
