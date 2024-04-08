package org.example.iotbay;

import org.springframework.stereotype.Service;


public interface UserService {
    User createUser(User user);
    boolean loginUser(String email, String password);

    void logoutUser();


}
