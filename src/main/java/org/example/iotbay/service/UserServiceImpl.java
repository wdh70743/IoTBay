package org.example.iotbay.service;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.User;
import org.example.iotbay.dto.UserDTO.LoginRequest;
import org.example.iotbay.dto.UserDTO.Response;
import org.example.iotbay.dto.UserDTO.Request;
import org.example.iotbay.exception.ResourceNotFoundException;
import org.example.iotbay.exception.UserAlreadyExistException;
import org.example.iotbay.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public Response createUser(Request request) {
        boolean userExists = userRepository.findByEmail(request.getEmail()).isPresent();

        if (userExists) {
            throw new UserAlreadyExistException("User Already Exists");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(encodedPassword);
        User user = modelMapper.map(request, User.class);
        User createdUser = userRepository.save(user);
        return modelMapper.map(createdUser, Response.class);
    }

    @Override
    public Response loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Email or Password is incorrect");
        }
        return modelMapper.map(user, Response.class);
    }

    @Override
    public void logoutUser() {

    }
}