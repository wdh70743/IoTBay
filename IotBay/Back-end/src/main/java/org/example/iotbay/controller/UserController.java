package org.example.iotbay.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "User API")
public class UserController {

    private final UserServiceImpl userService;
    @PostMapping("/register")
    @Operation(summary = "Register a New User", description="Creates a new user account with the provided data")
    @Parameters({
            @Parameter(name = "email", description = "The email of the user", example = "wdh70743@gmail.com"),
            @Parameter(name = "password", description = "The password of the user", example = "abcd1234"),
            @Parameter(name = "firstName", description = "The first name of the user", example = "Dohun"),
            @Parameter(name = "lastName", description = "The last name of the user", example = "Won"),
            @Parameter(name = "phoneNumber", description = "The phone number of the user", example = "0432472354"),
            @Parameter(name = "role", description = "The role of the user", example = "CUSTOMER or STAFF"),
            @Parameter(name="address", description = "The address of the user", example = "172-176 Parramatta Road, Homebush, NSW")
    })
    public ResponseEntity<Response> register(@Validated @RequestBody Request request){
        return ResponseEntity.ok(userService.createUser(request));
    }
    @PostMapping("/login")
    @Operation(summary = "Login with Email and Password", description="Login with user's email and password")
    @Parameters({
            @Parameter(name = "email", description = "The email of the user", example = "wdh70743@gmail.com"),
            @Parameter(name = "password", description = "The password of the user", example = "abcd1234"),
    })
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PostMapping("/login-staff")
    @Operation(summary = "Login with Email and Password", description="Login with staff's email and password")
    @Parameters({
            @Parameter(name = "email", description = "The email of the staff", example = "wdh70743@gmail.com"),
            @Parameter(name = "password", description = "The password of the staff", example = "abcd1234"),
    })
    public ResponseEntity<Response> loginStaff(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(userService.loginStaff(loginRequest));
    }

}
