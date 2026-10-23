package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Validations.Interfaces.PasswordConfirmable;
import com.jabai.campustrack.Validations.Interfaces.PasswordMatches;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class UpdateUserPasswordRequestDto implements PasswordConfirmable {
  @NotBlank(message = "Current password is required.")
  private final String currentPassword;

  @NotBlank(message = "New Password is required.")
  @Size(min = 8, message = "New password must be at least 8 characters.")
  private final String password;

  @NotBlank(message = "Password confirmation is required.")
  private final String passwordConfirmation;

  public UpdateUserPasswordRequestDto(String currentPassword, String password, String passwordConfirmation) {
    this.currentPassword = currentPassword;
    this.password = password;
    this.passwordConfirmation = passwordConfirmation;
  }

  public String getCurrentPassword() { return currentPassword; }
  public String getPassword() { return password; }
  public String getPasswordConfirmation() { return passwordConfirmation; }
}
