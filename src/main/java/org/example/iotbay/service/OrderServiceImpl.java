package org.example.iotbay.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.Order;
import org.example.iotbay.domain.OrderLineItem;
import org.example.iotbay.domain.Product;
import org.example.iotbay.domain.User;
import org.example.iotbay.dto.OrderDTO;
import org.example.iotbay.exception.InsufficientStockException;
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
        order.setStatus("Pending");

        Order savedOrder = orderRepository.save(order);
        List<OrderLineItem> orderItems = request.getItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId()));
                    if (itemDto.getQuantity() > product.getQuantity()) {
                        throw new InsufficientStockException("Requested quantity " + itemDto.getQuantity() +
                                " exceeds available stock for product ID " + itemDto.getProductId());
                    }
                    OrderLineItem orderItem = new OrderLineItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemDto.getQuantity());
                    orderItem.setPrice((double)product.getPrice() * orderItem.getQuantity());
                    product.setQuantity(product.getQuantity() - itemDto.getQuantity());
                    return orderItem;
                }).collect(Collectors.toList());

        orderLineItemRepository.saveAll(orderItems);
        double totalPrice = orderItems.stream()
                .mapToDouble(OrderLineItem::getTotalPrice)
                .sum();

        List<OrderDTO.OrderLineItemDTO> itemsDto = orderItems.stream().map(item -> {
            OrderDTO.OrderLineItemDTO dto = new OrderDTO.OrderLineItemDTO();
            dto.setProductId(item.getProduct().getId()); // Manually set productId
            dto.setQuantity(item.getQuantity());
            return dto;
        }).collect(Collectors.toList());
        savedOrder.setItems(new HashSet<>(orderItems));
        orderRepository.save(savedOrder);

        OrderDTO.Response response = modelMapper.map(savedOrder, OrderDTO.Response.class);
        response.setItems(itemsDto);
        response.setUserId(savedOrder.getUser().getId());
        response.setPrice(totalPrice);
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

        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));

        if (!existingOrder.getUser().getId().equals(request.getUserId())) {
            User user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));
            existingOrder.setUser(user);
        }
        existingOrder.setStatus("Pending");

        existingOrder.getItems().clear();

        List<OrderLineItem> updatedItems = request.getItems().stream()
                .map(itemDto -> {
                    Product product = productRepository.findById(itemDto.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemDto.getProductId()));
                    OrderLineItem orderItem = new OrderLineItem();
                    orderItem.setOrder(existingOrder);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemDto.getQuantity());
                    return orderItem;
                }).collect(Collectors.toList());

        existingOrder.getItems().addAll(updatedItems);

        Order updatedOrder = orderRepository.save(existingOrder);

        List<OrderDTO.OrderLineItemDTO> itemsDto = updatedItems.stream().map(item -> {
            OrderDTO.OrderLineItemDTO dto = new OrderDTO.OrderLineItemDTO();
            dto.setProductId(item.getProduct().getId());
            dto.setQuantity(item.getQuantity());
            return dto;
        }).collect(Collectors.toList());

        OrderDTO.Response response = modelMapper.map(updatedOrder, OrderDTO.Response.class);
        response.setItems(itemsDto);
        response.setUserId(updatedOrder.getUser().getId());

        return response;
    }



    @Override
    public String deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));
        order.getItems().forEach(item -> {
            Product product = item.getProduct();
            int newQuantity = product.getQuantity() + item.getQuantity();
            product.setQuantity(newQuantity);
            productRepository.save(product);
        });
        orderRepository.delete(order);
        return "Order successfully deleted";
    }
}
