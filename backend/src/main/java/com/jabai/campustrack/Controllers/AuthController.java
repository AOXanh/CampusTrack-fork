package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.LoginUserRequestDto;
import com.jabai.campustrack.DTOs.Requests.RegisterUserRequestDto;
import com.jabai.campustrack.DTOs.Responses.LoginUserResponseDto;
import com.jabai.campustrack.DTOs.Responses.RegisterUserResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.jabai.campustrack.Services.UserService;

import jakarta.validation.Valid;

/**
 *
 * <h4>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</h4>
 *
 * <p>JADE: Continue sad dri</p>
 *
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> registerUser(@Valid @RequestBody RegisterUserRequestDto registerUserRequestDto) {
        RegisterUserResponseDto responseDto = userService.registerUser(registerUserRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponseDto> loginUser(@Valid @RequestBody LoginUserRequestDto loginUserRequestDto) {
        LoginUserResponseDto responseDto = userService.loginUser(loginUserRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    /**
     * METHOD LEVEL AUTHORIZATION EXAMPLES
     * Mga example ni sya bai
     */

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-only-test")
    public ResponseEntity<String> adminRaniSyaBai() {
        return ResponseEntity.ok().body("Hello world daw bai ana si admin");
    }

    @PreAuthorize("hasRole('WORKER')")
    @GetMapping("/worker-only-test")
    public ResponseEntity<String> workerRaniSyaBai() {
        return ResponseEntity.ok().body("Hello world daw bai ana si worker");
    }

    @PreAuthorize("hasRole('STUDENT')") // CHANGED IT FOR FORMALITY, I GUESS XD
    @GetMapping("/user-only-test")
    public ResponseEntity<String> userRaniSyaBai() {
        return ResponseEntity.ok().body("Hello world daw bai ana si user");
    }
}
