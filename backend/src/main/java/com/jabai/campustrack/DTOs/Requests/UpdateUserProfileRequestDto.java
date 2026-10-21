package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateUserProfileRequestDto {

  @Pattern (regexp = ".*\\S.*", message = "Name must not be blank.")
  @Size(max = 100, message = "Name must be 100 characters or fewer.")
  private final String name;

@Pattern(regexp = ".*\\S.*", message = "Email must not be blank.")
@Email(message = "Please enter a valid email.")
@Size(max = 100, message = "Email must be 100 characters or fewer.")
private final String email;

  public UpdateUserProfileRequestDto(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String getName() { return name; }
  public String getEmail() { return email; }
}
