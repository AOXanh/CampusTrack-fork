package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Requests.LoginUserRequestDto;
import com.jabai.campustrack.DTOs.Requests.RegisterUserRequestDto;
import com.jabai.campustrack.DTOs.Responses.LoginUserResponseDto;
import com.jabai.campustrack.DTOs.Responses.RegisterUserResponseDto;
import com.jabai.campustrack.DTOs.Responses.UserProfileResponseDto;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jabai.campustrack.Repositories.UserRepository;
import com.jabai.campustrack.Exceptions.CustomExceptions.EmailAlreadyExistException;
import com.jabai.campustrack.Exceptions.CustomExceptions.EmailNotFoundException;
import com.jabai.campustrack.Exceptions.CustomExceptions.InvalidCredentialsException;
import com.jabai.campustrack.Securities.JwtUtil;
import com.jabai.campustrack.Models.*;
import com.jabai.campustrack.Models.Enums.UserRole;

import java.util.Optional;

@Service 
public class UserService {
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository; 

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public RegisterUserResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto) {
        if (userRepository.findByEmail(registerUserRequestDto.getEmail()).isPresent())
            throw new EmailAlreadyExistException("Email already exists");

        String hashedPassword = passwordEncoder.encode(registerUserRequestDto.getPassword());

        User user = new User(
            registerUserRequestDto.getName(),
            registerUserRequestDto.getEmail(),
            hashedPassword,
            UserRole.USER
        );

        userRepository.save(user);
        return new RegisterUserResponseDto("Your account has successfully been created.");
    }

    public LoginUserResponseDto loginUser(LoginUserRequestDto loginUserRequestDto) throws RuntimeException {
        Optional<User> foundUser = userRepository.findByEmail(loginUserRequestDto.getEmail());

        if (foundUser.isEmpty())
            throw new EmailNotFoundException("Unable to find user.");
        if (!passwordEncoder.matches(loginUserRequestDto.getPassword(), foundUser.get().getHashedPassword()))
            throw new InvalidCredentialsException("Your email or password is incorrect.");

        User currentUser = foundUser.get();
        String userToken = jwtUtil.generateToken(currentUser);

        return new LoginUserResponseDto(
                "Login success!",
                currentUser.getName(),
                currentUser.getEmail(),
                userToken
        );
    }

    public UserProfileResponseDto getUserProfile(String email){ 
        User user =  userRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email not found")); 

        return  new UserProfileResponseDto(
            user.getName(),
            user.getEmail(),
            user.getUserRole()
        ); 
    }




}
