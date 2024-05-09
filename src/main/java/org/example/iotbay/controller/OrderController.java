package org.example.iotbay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.dto.OrderDTO;
import org.example.iotbay.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
@Tag(name= "order", description = "Order API")

public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Create a new order", description = "Creates a new order with the specified details.")
    @Parameters({
            @Parameter(name = "userId", description = "The user Id requesting the order", example = "1"),

    })
    public ResponseEntity<OrderDTO.Response> createOrder(@Validated @RequestBody OrderDTO.Request orderRequest) {
        OrderDTO.Response response = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Get order details", description = "Retrieves details of a specific order by its ID.")
    public ResponseEntity<OrderDTO.Response> getOrderById(@PathVariable Long orderId) {
        OrderDTO.Response response = orderService.getOrderById(orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List orders by user", description = "Retrieves all orders associated with a specific user ID.")
    public ResponseEntity<List<OrderDTO.Response>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDTO.Response> responses = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{orderId}")
    @Operation(summary = "Update an order", description = "Updates an existing order with new information based on the provided order ID.")
    public ResponseEntity<OrderDTO.Response> updateOrder(@PathVariable Long orderId, @Validated @RequestBody OrderDTO.Request orderRequest) {
        OrderDTO.Response response = orderService.updateOrder(orderId, orderRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete an order", description = "Deletes an existing order by its ID.")
    public ResponseEntity<String> deleteOrder(@PathVariable Long orderId) {
        String result = orderService.deleteOrder(orderId);
        return ResponseEntity.ok(result);
    }
}
