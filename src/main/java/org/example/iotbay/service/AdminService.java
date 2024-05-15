package org.example.iotbay.service;

import org.example.iotbay.dto.UserDTO.AdminRequest;
import org.example.iotbay.dto.UserDTO.Response;

public interface AdminService {

    Response createUser(AdminRequest request);

    Response updateUserDetails(Long id, AdminRequest request);

    String deleteUser(Long id, Long adminId);

    Response getUserDetails(Long id);

    String activateUser(Long userId, Long adminId);

    String deactivateUser(Long userId, Long adminId);
}
