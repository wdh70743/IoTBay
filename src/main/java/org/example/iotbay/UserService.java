package com.VisaPeople.IoTBay;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User user);
    boolean loginUser(String email, String password);

    void logoutUser();


}
