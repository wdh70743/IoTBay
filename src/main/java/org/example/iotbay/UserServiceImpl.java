package org.example.iotbay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        User user2 = userRepository.findByEmail(user.getEmail());
        if(user2 != null) {
            return null;
        }
        return userRepository.save(user);
    }

    @Override
    public boolean loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        return user != null && Objects.equals(user.getPassword(), password);
    }

    @Override
    public void logoutUser() {

    }
}