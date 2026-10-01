package com.jabai.campustrack.DTOs.Responses;

public class CreateRoomResponseDto {
    private final String message;
    
    public CreateRoomResponseDto(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
