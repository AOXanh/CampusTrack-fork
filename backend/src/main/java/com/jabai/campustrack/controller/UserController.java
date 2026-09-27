package com.jabai.campustrack.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.jabai.campustrack.model.*;

import com.jabai.campustrack.services.UserService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/auth")
public class UserController {

    private final UserService userService; 

    public UserController(UserService userService){ 
        this.userService = userService; 
    }

    @PostMapping("/register")
    public String registerUser(@Valid @RequestBody User user){ 
    return  userService.registerUser(user); 
    }

    @PostMapping("/login")
    public String loginUser(@Valid @RequestBody User user){ 
    return  userService.loginUser(user); 
    }

}
