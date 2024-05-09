package org.example.iotbay.service;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.User;
import org.example.iotbay.domain.UserLog;
import org.example.iotbay.dto.UserDTO.LoginRequest;
import org.example.iotbay.dto.UserDTO.Response;
import org.example.iotbay.dto.UserDTO.Request;
import org.example.iotbay.exception.EmailAlreadyExistException;
import org.example.iotbay.exception.ResourceNotFoundException;
import org.example.iotbay.exception.UnauthorizedAccessException;
import org.example.iotbay.exception.UserAlreadyExistException;
import org.example.iotbay.repository.UserLogRepository;
import org.example.iotbay.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserLogRepository userLogRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    private void saveLog(User user, String action){
        UserLog userLog = new UserLog(
                user.getId(),
                user,
                action
        );
        userLogRepository.save(userLog);
    }

    @Override
    public Response createUser(Request request) {
        boolean userExists = userRepository.findByEmail(request.getEmail()).isPresent() || userRepository.findByPhoneNumber(request.getPhoneNumber()).isPresent();

        if (userExists) {
            throw new UserAlreadyExistException("An account with the provided credentials already exists.");
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
        saveLog(user, "SIGN-IN");
        return modelMapper.map(user, Response.class);
    }

    @Override
    public Response loginStaff(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Email or Password is incorrect");
        }
        if (!Objects.equals(user.getRole(), "STAFF")){
            throw new UnauthorizedAccessException("User is not authorized as staff member");
        }
        saveLog(user, "SIGN-IN");
        return modelMapper.map(user, Response.class);
    }

    @Override
    public String logoutUser(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Email or Password is incorrect");
        }
        saveLog(user, "SIGN-OUT");
        return "Logout Successfully";
    }

    @Override
    public String logoutStaff(LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Email or Password is incorrect");
        }
        if (!Objects.equals(user.getRole(), "STAFF")){
            throw new UnauthorizedAccessException("User is not authorized as staff member");
        }
        saveLog(user, "SIGN-OUT");
        return "Logout Successfully";
    }

    @Override
    public Response getUserDetails(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return modelMapper.map(user, Response.class);
    }

    @Override
    public Set<UserLog> getUserLogs(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return user.getUserLogs();
    }

    @Override
    public Response updateUserDetails(Long id, Request request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        Optional<User> duplicatedEmailUser = userRepository.findByEmail(request.getEmail());

        if (duplicatedEmailUser.isPresent() && !duplicatedEmailUser.get().getId().equals(user.getId())) {
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
    public String deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        userRepository.delete(user);
        return "User successfully deleted";
    }
}