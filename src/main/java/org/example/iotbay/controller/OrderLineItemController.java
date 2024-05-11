package org.example.iotbay.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.dto.OrderDTO;
import org.example.iotbay.service.OrderLineItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order-line-items")
@Tag(name = "Order Line Items", description = "API for managing order line items")
public class OrderLineItemController {
    private final OrderLineItemService orderLineItemService;

    @PostMapping("/create")
    @Operation(summary = "Add new order line items", description = "Adds new line items to a specific order.")
    public ResponseEntity<List<OrderDTO.OrderLineItemDTO>> createOrderLineItems(@RequestParam Long orderId, @Validated @RequestBody List<OrderDTO.OrderLineItemDTO> itemsDto) {
        List<OrderDTO.OrderLineItemDTO> response = orderLineItemService.createOrderLineItems(orderId, itemsDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get order line items", description = "Retrieves all line items for a specific order.")
    public ResponseEntity<List<OrderDTO.OrderLineItemDTO>> getOrderLineItemsByOrderId(@PathVariable Long orderId) {
        List<OrderDTO.OrderLineItemDTO> response = orderLineItemService.getOrderLineItemsByOrderId(orderId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{itemID}")
    @Operation(summary = "Update an order line item", description = "Updates a specific order line item by ID.")
    public ResponseEntity<OrderDTO.OrderLineItemDTO> updateOrderItem(@PathVariable Long itemID, @Validated @RequestBody OrderDTO.OrderLineItemDTO itemDto) {
        OrderDTO.OrderLineItemDTO response = orderLineItemService.updateOrderItem(itemID, itemDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{itemID}")
    @Operation(summary = "Delete an order line item", description = "Deletes a specific order line item by ID.")
    public ResponseEntity<String> deleteOrderItem(@PathVariable Long itemID) {
        String result = orderLineItemService.deleteOrderItem(itemID);
        return ResponseEntity.ok(result);
    }
}
