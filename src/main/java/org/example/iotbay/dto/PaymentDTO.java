package org.example.iotbay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.iotbay.domain.Order;

import java.time.LocalDateTime;

public class PaymentDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Request{
        private Long orderId;
        private String paymentMethod;
        private String cardLastFourDigits;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private Long id;
        private Long orderId;
        private Double amount;
        private String paymentMethod;
        private String cardLastFourDigits;
        private String paymentStatus;
        private LocalDateTime createdAt;
    }
}
