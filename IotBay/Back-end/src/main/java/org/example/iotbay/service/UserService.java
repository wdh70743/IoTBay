package org.example.iotbay.service;

import org.example.iotbay.domain.User;
import org.example.iotbay.dto.UserDTO.LoginRequest;
import org.example.iotbay.dto.UserDTO.Request;
import org.example.iotbay.dto.UserDTO.Response;


public interface UserService {
    Response createUser(Request request);
    Response loginUser(LoginRequest loginRequest);

    Response loginStaff(LoginRequest loginRequest);




}
