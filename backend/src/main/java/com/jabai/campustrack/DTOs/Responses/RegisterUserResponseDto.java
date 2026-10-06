package com.jabai.campustrack.DTOs.Responses;

public class RegisterUserResponseDto {
  private final String message;

  public RegisterUserResponseDto(String message) {
    this.message = message;
  }

  public String getMessage() { return message; }
}