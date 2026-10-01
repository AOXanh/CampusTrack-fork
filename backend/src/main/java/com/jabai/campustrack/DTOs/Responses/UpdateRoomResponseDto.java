package com.jabai.campustrack.DTOs.Responses;

public class UpdateRoomResponseDto {
    private final String message;
    
    public UpdateRoomResponseDto(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
