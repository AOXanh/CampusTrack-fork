package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Requests.LoginUserRequestDto;
import com.jabai.campustrack.DTOs.Requests.RegisterUserRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateUserPasswordRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateUserProfileRequestDto;
import com.jabai.campustrack.DTOs.Responses.LoginUserResponseDto;
import com.jabai.campustrack.DTOs.Responses.RegisterUserResponseDto;
import com.jabai.campustrack.DTOs.Responses.UserProfileResponseDto;

import com.jabai.campustrack.Exceptions.CustomExceptions.DuplicatedRowException;
import com.jabai.campustrack.Exceptions.CustomExceptions.RowNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jabai.campustrack.Repositories.UserRepository;
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

    /**
     * ========== AUTHS ==========
     * Para sa auth controller
     */

    // Register user
    public RegisterUserResponseDto registerUser(RegisterUserRequestDto registerUserRequestDto) {
        if (userRepository.findByEmail(registerUserRequestDto.getEmail()).isPresent())
            throw new DuplicatedRowException("This email already exists.");

        String hashedPassword = passwordEncoder.encode(registerUserRequestDto.getPassword());

        User user = new User(
            registerUserRequestDto.getName(),
            registerUserRequestDto.getEmail(),
            hashedPassword,
            UserRole.STUDENT
        );

        userRepository.save(user);
        return new RegisterUserResponseDto("Your account has successfully been created.");
    }

    // Login user
    public LoginUserResponseDto loginUser(LoginUserRequestDto loginUserRequestDto) throws RuntimeException {
        Optional<User> foundUser = userRepository.findByEmail(loginUserRequestDto.getEmail());

        if (foundUser.isEmpty())
            throw new RowNotFoundException("Unable to find user.");
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

    /**
     * ========== PROFILES ==========
     * Para sa profile controller
     */

    // Get
    public UserProfileResponseDto getUserProfile(Long id) {
        User user =  userRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException("Email not found."));

        return buildUserProfileResponseDto(user);
    }

    // Update
    public UserProfileResponseDto updateUserProfile(Long id, UpdateUserProfileRequestDto updateUserProfileRequestDto) {
        User foundUser = userRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException("Unable to find the user."));

        String name = updateUserProfileRequestDto.getName();
        String email =  updateUserProfileRequestDto.getEmail();

        if (name != null)
            foundUser.setName(name);
        if (email != null) {
            userRepository
                    .findByEmail(email)
                    .ifPresent(user -> {
                        if (!user.getId().equals(id))
                            throw new DuplicatedRowException("This email already exists.");
                    });
            foundUser.setEmail(email);
        }

        User response = userRepository.save(foundUser);
        return buildUserProfileResponseDto(response);
    }

    // Update password
    public UserProfileResponseDto updateUserPassword(Long id, UpdateUserPasswordRequestDto updateUserPasswordRequestDto) {
        User foundUser = userRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException("Unable to find the user."));

        if (!passwordEncoder.matches(updateUserPasswordRequestDto.getCurrentPassword(), foundUser.getHashedPassword()))
            throw new InvalidCredentialsException("Your current password doesn't matched");

        String hashedPassword = passwordEncoder.encode(updateUserPasswordRequestDto.getPassword());
        foundUser.setHashedPassword(hashedPassword);

        User user = userRepository.save(foundUser);
        return buildUserProfileResponseDto(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        User foundUser = userRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException("Unable to find the user."));
        userRepository.delete(foundUser);
    }

    // ===== Service Utils =====
    private UserProfileResponseDto buildUserProfileResponseDto(User user) {
        return new UserProfileResponseDto(
                user.getCreatedAt(),
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getUserRole()
        );
    }
}
