package com.jabai.campustrack.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jabai.campustrack.repository.UserRepository;
import com.jabai.campustrack.model.Enums.*;
import com.jabai.campustrack.globalerrorhandler.EmailAlreadyExistException;
import com.jabai.campustrack.globalerrorhandler.EmailNotFoundException;
import com.jabai.campustrack.globalerrorhandler.InvalidCredentialsException;
import com.jabai.campustrack.jwt.JwtUtil;
import com.jabai.campustrack.model.*; 

@Service 
public class UserService {
    
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository; 

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil){ 
        this.userRepository = userRepository; 
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil; 
    }

    public String registerUser(User user){

         System.out.println("NAME: " + user.getName());
    System.out.println("EMAIL: " + user.getEmail());
    System.out.println("PASSWORD: " + user.getHashedPassword());

    if(userRepository.findByEmail(user.getEmail()).isPresent()){ 
        throw new EmailAlreadyExistException("Email already exists");
    }

    user.setHashedPassword(
        passwordEncoder.encode(user.getHashedPassword())
    );

    user.setUserRole(UserRole.USER);
    userRepository.save(user); 

    return "Registered successfully"; 
   /* 
   
   
    if(userRepository.findByEmail(user.getEmail()).isPresent()){ 
        throw new EmailAlreadyExistException("Email already exists");
     }
     user.setHashedPassword(passwordEncoder.encode(user.getHashedPassword()));
     user.setUserRole(UserRole.USER);
     userRepository.save(user); 
        return "Registered successfully"; 
        */ 
    }

    public String loginUser(User user){ 
        User currentUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new EmailNotFoundException("Email not found"));
        if(!passwordEncoder.matches(user.getHashedPassword(), currentUser.getHashedPassword())){    
            throw new InvalidCredentialsException("Email or Password is incorrect"); 
        }
      return   jwtUtil.generateToken(currentUser); 
    }











}
