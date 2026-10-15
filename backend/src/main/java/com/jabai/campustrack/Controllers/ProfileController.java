package com.jabai.campustrack.Controllers;


import com.jabai.campustrack.DTOs.Responses.UserProfileResponseDto;
import com.jabai.campustrack.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h4>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</h4>
 *
 * <p>JADE: Continue sad dri</p>
 * <p>Naa nakoy gi buhat nga mga services para sa update, change password and delete palihug lang ko ug connect.</p>
 *
 */
@RestController
@RequestMapping("/api/profile")
public class ProfileController {
  private final UserService userService;

  public ProfileController(UserService userService) {
    this.userService = userService;
  }

  // Get
  @GetMapping("/me")
  public ResponseEntity<UserProfileResponseDto> me(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    UserProfileResponseDto response = userService.getUserProfile(userId);
    return ResponseEntity.ok().body(response);
  }

  // Update

  // Change password

  // Delete
}
