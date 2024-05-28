package org.example.iotbay.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.dto.PaymentDTO;
import org.example.iotbay.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
@Tag(name = "Payments", description = "API for managing payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create")
    @Operation(
            summary = "Create a payment",
            description = "Creates a new payment record for an order.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                    {
                      "orderId": 1,
                      "paymentMethod": "Credit Card",
                      "cardLastFourDigits": "1234"
                    }
                    """
                            )
                    )
            )
    )
    public ResponseEntity<PaymentDTO.Response> createPayment(
            @Validated @RequestBody PaymentDTO.Request request
    ) {
        PaymentDTO.Response response = paymentService.createPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{paymentId}")
    @Operation(
            summary = "Get a payment",
            description = "Retrieves a payment by its ID.",
            parameters = {
                    @Parameter(name = "paymentId", description = "The ID of the payment to retrieve", example = "1")
            }
    )
    public ResponseEntity<PaymentDTO.Response> getPaymentById(
            @PathVariable Long paymentId
    ) {
        PaymentDTO.Response response = paymentService.getPaymentById(paymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payments by order", description = "Retrieves all payments for a specific order.")
    public ResponseEntity<PaymentDTO.Response> getPaymentsByOrderId(@PathVariable Long orderId) {
        PaymentDTO.Response responses = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{paymentId}")
    @Operation(
            summary = "Update a payment",
            description = "Updates an existing payment record.",
            parameters = {
                    @Parameter(name = "paymentId", description = "The ID of the payment to update", example = "1")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                    {
                      "orderId": 1,
                      "paymentMethod": "Credit Card",
                      "cardLastFourDigits": "5678"
                    }
                    """
                            )
                    )
            )
    )
    public ResponseEntity<PaymentDTO.Response> updatePayment(
            @PathVariable Long paymentId,
            @Validated @RequestBody PaymentDTO.Request request
    ) {
        PaymentDTO.Response response = paymentService.updatePayment(paymentId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{paymentId}")
    @Operation(
            summary = "Delete a payment",
            description = "Deletes a payment record by its ID.",
            parameters = {
                    @Parameter(name = "paymentId", description = "The ID of the payment to delete", example = "1")
            }
    )
    public ResponseEntity<String> deletePayment(
            @PathVariable Long paymentId
    ) {
        String result = paymentService.deletePayment(paymentId);
        return ResponseEntity.ok(result);
    }

}
