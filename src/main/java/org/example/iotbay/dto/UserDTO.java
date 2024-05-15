package org.example.iotbay.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

public class UserDTO {
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Request {
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String role;
        private String address;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class Response {
        private Long id;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String role;
        private String address;
        private LocalDateTime createdAt;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class AdminRequest {
        private Long adminId;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String role;
        private String address;
    }

}
