package org.example.iotbay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.Order;
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
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    @Operation(
            summary = "Create a new order",
            description = "Creates a new order with the specified details.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                    {
                      "userId": 1,
                      "items": [
                        {
                          "productId": 1,
                          "quantity": 2
                        },
                        {
                          "productId": 2,
                          "quantity": 3
                        }
                      ]
                    }
                    """
                            )
                    )
            )
    )
    public ResponseEntity<OrderDTO.Response> createOrder(
            @Validated @RequestBody OrderDTO.Request orderRequest
    ) {
        OrderDTO.Response response = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    @Operation(
            summary = "Get order details",
            description = "Retrieves details of a specific order by its ID.",
            parameters = {
                    @Parameter(name = "orderId", description = "The ID of the order to retrieve", example = "1")
            }
    )
    public ResponseEntity<Order> getOrderById(
            @PathVariable Long orderId
    ) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "List orders by user",
            description = "Retrieves all orders associated with a specific user ID.",
            parameters = {
                    @Parameter(name = "userId", description = "The ID of the user whose orders are to be retrieved", example = "1")
            }
    )
    public ResponseEntity<List<Order>> getOrdersByUserId(
            @PathVariable Long userId
    ) {
        List<Order> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}")
    @Operation(
            summary = "Update an order",
            description = "Updates an existing order with new information based on the provided order ID.",
            parameters = {
                    @Parameter(name = "orderId", description = "The ID of the order to update", example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                    {
                      "userId": 1,
                      "items": [
                        {
                          "productId": 1,
                          "quantity": 2
                        },
                        {
                          "productId": 2,
                          "quantity": 3
                        }
                      ]
                    }
                    """
                            )
                    )
            )
    )
    public ResponseEntity<OrderDTO.Response> updateOrder(
            @PathVariable Long orderId,
            @Validated @RequestBody OrderDTO.Request orderRequest
    ) {
        OrderDTO.Response response = orderService.updateOrder(orderId, orderRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    @Operation(
            summary = "Delete an order",
            description = "Deletes an existing order by its ID.",
            parameters = {
                    @Parameter(name = "orderId", description = "The ID of the order to delete", example = "1")
            }
    )
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long orderId
    ) {
        String result = orderService.deleteOrder(orderId);
        return ResponseEntity.ok(result);
    }
}
