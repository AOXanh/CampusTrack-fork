package com.jabai.campustrack.DTOs.Requests;

public class UpdateUserProfileRequestDto {
  private final String name;
  private final String email;

  public UpdateUserProfileRequestDto(String name, String email) {
    this.name = name;
    this.email = email;
  }

  public String getName() { return name; }
  public String getEmail() { return email; }
}
