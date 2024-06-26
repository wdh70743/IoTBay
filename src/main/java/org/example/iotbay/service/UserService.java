package org.example.iotbay.service;

import org.example.iotbay.domain.User;
import org.example.iotbay.domain.UserLog;
import org.example.iotbay.dto.UserDTO.LoginRequest;
import org.example.iotbay.dto.UserDTO.Request;
import org.example.iotbay.dto.UserDTO.Response;

import java.util.ArrayList;
import java.util.Set;


public interface UserService {
    Response createUser(Request request);
    Response loginUser(LoginRequest loginRequest);

    Response loginStaff(LoginRequest loginRequest);

    Response loginAdmin(LoginRequest loginRequest);

    String logoutUser(LoginRequest loginRequest);

    String logoutStaff(LoginRequest loginRequest);

    String logoutAdmin(LoginRequest loginRequest);


    Response getUserDetails(Long id);

    Set<UserLog> getUserLogs(Long id);

    Response updateUserDetails(Long id, Request request);

    String deleteUser(Long id);
}
