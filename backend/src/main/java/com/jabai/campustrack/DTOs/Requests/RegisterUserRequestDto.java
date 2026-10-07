package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Enums.UserRole;
import com.jabai.campustrack.Validations.Interfaces.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@PasswordMatches
public class RegisterUserRequestDto {
  @NotBlank(message = "Name is required.")
  private final String name;

  @NotBlank(message = "Email is required.")
  @Email(message = "Please provide a valid email address.")
  private final String email;

  @NotBlank(message = "Password is required.")
  @Size(min = 8, message = "Password must be at least 8 characters.")
  private final String password;

  @NotBlank(message = "Password confirmation is required.")
  private final String passwordConfirmation;

  public RegisterUserRequestDto(String name, String email, String password, String passwordConfirmation) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.passwordConfirmation = passwordConfirmation;
  }

  // Getters
  public String getName() { return name; }
  public String getEmail() { return email; }
  public String getPassword() { return password; }
  public String getPasswordConfirmation() { return passwordConfirmation; }
}
