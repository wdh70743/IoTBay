package com.VisaPeople.IoTBay;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User createUser(User user){
        User user2 = userRepository.findByEmail(user.getEmail());
        if(user2 != null) {
            return null;
        }
        return userRepository.save(user);
    }


    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && Objects.equals(user.getPassword(), password);
    }


}
