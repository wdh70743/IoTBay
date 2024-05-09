package org.example.iotbay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProductDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Request{
        private Long userId;
        private String name;
        private String description;
        private String type;
        private Integer quantity;
        private Integer price;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Response{
        private Long id;
        private String name;
        private String description;
        private String type;
        private Integer quantity;
        private Integer price;
        private LocalDateTime createdAt;
    }
}
