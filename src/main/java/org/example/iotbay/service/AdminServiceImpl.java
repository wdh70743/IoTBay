package org.example.iotbay.service;

import java.util.Objects;
import java.util.Optional;

import org.example.iotbay.domain.User;
import org.example.iotbay.dto.UserDTO.AdminRequest;
import org.example.iotbay.dto.UserDTO.Response;
import org.example.iotbay.exception.EmailAlreadyExistException;
import org.example.iotbay.exception.ResourceNotFoundException;
import org.example.iotbay.exception.UnauthorizedAccessException;
import org.example.iotbay.exception.UserAlreadyExistException;
import org.example.iotbay.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public boolean isAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NullPointerException("User does not exist."));

        return Objects.equals(user.getRole(), "ADMIN");
    }

    @Override
    public Response createUser(AdminRequest request) {
        if (!isAdmin(request.getAdminId())) {
            throw new UnauthorizedAccessException("Only admins can create users.");
        }

        // Check if user already exists
        if (userRepository.findByEmail(request.getEmail()).isPresent()
                || userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
            throw new UserAlreadyExistException("An account with the provided credentials already exists.");
        }

        // Encode the password
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);

        // Save the User
        User user = modelMapper.map(request, User.class);
        User createdUser = userRepository.save(user);
        return modelMapper.map(createdUser, Response.class);

    }

    @Override
    public Response updateUserDetails(Long id, AdminRequest request) {

        if (!isAdmin(request.getAdminId())) {
            throw new UnauthorizedAccessException("User is not authorized as Admin member");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Optional<User> deplicatedEmailUser = userRepository.findByEmail(request.getEmail());

        if (deplicatedEmailUser.isPresent() && !deplicatedEmailUser.get().getId().equals(user.getId())) {
            throw new EmailAlreadyExistException("Email already exists");
        }

        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        userRepository.save(user);
        return modelMapper.map(user, Response.class);
    }

    @Override
    public String deleteUser(Long id, Long adminId) {
        if (!isAdmin(adminId)) {
            throw new UnauthorizedAccessException("User is not authorized as Admin member");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        userRepository.delete(user);
        return "User successfully deleted";
    }

    @Override
    public Response getUserDetails(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return modelMapper.map(user, Response.class);
    }

    @Override
    public String activateUser(Long userId, Long adminId) {
        if (!isAdmin(adminId)) {
            throw new UnauthorizedAccessException("User is not authorized as Admin member");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        user.setActive(true);
        userRepository.save(user);
        System.out.println(user.isActive());
        return "User successfully activated";
    }

    @Override
    public String deactivateUser(Long userId, Long adminId) {

        if (!isAdmin(adminId)) {
            throw new UnauthorizedAccessException("User is not authorized as Admin member");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        user.setActive(false);
        System.out.println(user.isActive());
        userRepository.save(user);
        return "User successfully deactivated";
    }

}

