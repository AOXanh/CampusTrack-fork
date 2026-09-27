package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class LoginUserRequestDto {
  @NotBlank(message = "Email is required.")
  @Email(message = "Please provide a valid email address.")
  private final String email;

  @NotBlank(message = "Password is required.")
  private final String password;

  public LoginUserRequestDto(String email, String password) {
    this.email = email;
    this.password = password;
  }

  // Getters
  public String getEmail() { return email; }
  public String getPassword() { return password; }
}
