package com.jabai.campustrack.DTOs.Responses;

public class LoginUserResponseDto {
  private final String message;
  private final String name;
  private final String email;
  private final String token;

  public LoginUserResponseDto(String message, String name, String email, String token) {
    this.message = message;
    this.name = name;
    this.email = email;
    this.token = token;
  }

  public LoginUserResponseDto(String message) {
    this.message = message;
    this.name = null;
    this.email = null;
    this.token = null;
  }

  // Getters
  public String getMessage() { return message; }
  public String getName() { return name; }
  public String getEmail() { return email; }
  public String getToken() { return token; }
}
