package org.example.iotbay.controller;

import org.example.iotbay.dto.UserDTO.AdminRequest;
import org.example.iotbay.dto.UserDTO.Response;
import org.example.iotbay.service.AdminServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "Admin API")
public class AdminController {
    private final AdminServiceImpl adminService;

    @PostMapping("/adminCreate")
    @Operation(summary = "Create a New User", description = "Admin creates a new user account with the provided data")
    @Parameters({
            @Parameter(name = "adminId", description = "The ID of the admin creating the user", example = "1"),
            @Parameter(name = "email", description = "The email of the user", example = "ray.kinjo@gmail.com"),
            @Parameter(name = "password", description = "The password of the user", example = "abcd1234"),
            @Parameter(name = "firstName", description = "The first name of the user", example = "Ray"),
            @Parameter(name = "lastName", description = "The last name of the user", example = "Kinjo"),
            @Parameter(name = "phoneNumber", description = "The phone number of the user", example = "0413946927"),
            @Parameter(name = "role", description = "The role of the user", example = "ADMIN or CUSTOMER or STAFF"),
            @Parameter(name = "address", description = "The address of the user", example = "702-730 Harris St, Ultimo, NSW")
    })
    public ResponseEntity<Response> createUser(@Validated @RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminService.createUser(request));
    }

    @PutMapping("/adminUpdate/{id}")
    @Operation(summary = "Update User Details", description = "Admin Update details ofr s user based on the provided data")
    @Parameters({
            @Parameter(name = "adminId", description = "The ID of the admin creating the user", example = "1"),
            @Parameter(name = "email", description = "The email of the user", example = "ray.kinjo@gmail.com"),
            @Parameter(name = "password", description = "The password of the user", example = "abcd1234"),
            @Parameter(name = "firstName", description = "The first name of the user", example = "Ray"),
            @Parameter(name = "lastName", description = "The last name of the user", example = "Kinjo"),
            @Parameter(name = "phoneNumber", description = "The phone number of the user", example = "0413946927"),
            @Parameter(name = "role", description = "The role of the user", example = "ADMIN or CUSTOMER or STAFF"),
            @Parameter(name = "address", description = "The address of the user", example = "702-730 Harris St, Ultimo, NSW")
    })

    public ResponseEntity<Response> updateUserDetails(@PathVariable("id") Long id, @RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminService.updateUserDetails(id, request));
    }

    @DeleteMapping("/adminDelete/{id}")
    @Operation(summary = "Delete User", description = "Deletes a user from the system by their unique identifier")
    @Parameters({
            @Parameter(name = "id", description = "The unique identifier of the user", example = "1"),
            @Parameter(name = "requestedBy", description = "The ID of the Admin requesting the deletion", example = "2")
    })
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id, @RequestParam("requestedBy") Long adminId) {
        return ResponseEntity.ok(adminService.deleteUser(id, adminId));
    }

    @GetMapping("/adminRetrive/{id}")
    @Operation(summary = "Retrieve User Details", description = "Fetches details of a user by their unique identifier")
    @Parameters({
            @Parameter(name = "id", description = "The unique identifier of the user", example = "1")
    })
    public ResponseEntity<Response> getUserDetails(@PathVariable("id") Long id) {
        return ResponseEntity.ok(adminService.getUserDetails(id));
    }

    @GetMapping("/adminActivate/{id}")
    @Operation(summary = "set user active", description = "Admin sets User Active by their unique identifier")
    @Parameters({
            @Parameter(name = "id", description = "The unique identifier of the user", example = "1"),
            @Parameter(name = "requestedBy", description = "The ID of the Admin requesting the deletion", example = "2")
    })
    public ResponseEntity<String> activateUser(@PathVariable("id") Long id, @RequestParam("requestedBy") Long adminId) {
        return ResponseEntity.ok(adminService.activateUser(id, adminId));
    }

    @GetMapping("/adminDeactivate/{id}")
    @Operation(summary = "set user deactivate", description = "Admin sets User Active by their unique identifier")
    @Parameters({
            @Parameter(name = "id", description = "The unique identifier of the user", example = "1"),
            @Parameter(name = "requestedBy", description = "The ID of the Admin requesting the deletion", example = "2")
    })
    public ResponseEntity<String> deactivateUser(@PathVariable("id") Long id, @RequestParam("requestedBy") Long adminId) {
        return ResponseEntity.ok(adminService.deactivateUser(id, adminId));
    }

}