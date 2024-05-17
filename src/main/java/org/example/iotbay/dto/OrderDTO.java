package org.example.iotbay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Request{
        private long userId;
        private List<OrderLineItemDTO> items;

    }
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private Long id;
        private Long userId;
        private List<OrderLineItemDTO> items;
        private double price;
        private String status;
        private LocalDateTime createdAt;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class OrderLineItemDTO {
        private Long productId;
        private Integer quantity;
    }
}
