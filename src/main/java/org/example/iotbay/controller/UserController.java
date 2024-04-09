package org.example.iotbay.controller;

import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.User;
import org.example.iotbay.dto.UserDTO.LoginRequest;
import org.example.iotbay.dto.UserDTO.Request;
import org.example.iotbay.dto.UserDTO.Response;
import org.example.iotbay.service.UserService;
import org.example.iotbay.service.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserServiceImpl userService;
    @PostMapping("/register")
    public ResponseEntity<Response> register(@Validated @RequestBody Request request){
        return ResponseEntity.ok(userService.createUser(request));
    }
    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

}
