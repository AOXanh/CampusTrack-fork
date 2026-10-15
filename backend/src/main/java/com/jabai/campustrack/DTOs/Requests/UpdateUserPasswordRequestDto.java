package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateUserPasswordRequestDto {
  @NotBlank(message = "Password is required.")
  @Size(min = 8, message = "Password must be at least 8 characters.")
  private final String password;

  @NotBlank(message = "Password confirmation is required.")
  private final String passwordConfirmation;

  public UpdateUserPasswordRequestDto(String password, String passwordConfirmation) {
    this.password = password;
    this.passwordConfirmation = passwordConfirmation;
  }

  public String getPassword() { return password; }
  public String getPasswordConfirmation() { return passwordConfirmation; }
}
