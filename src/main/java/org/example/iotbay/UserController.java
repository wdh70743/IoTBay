package org.example.iotbay;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public String register(User user){
        User user1 = userService.createUser(user);
        if(user1 != null){
            return "register_success";
        }
        //error
        return "register";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "register";
    }



}
