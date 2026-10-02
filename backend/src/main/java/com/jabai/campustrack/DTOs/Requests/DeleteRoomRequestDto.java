package com.jabai.campustrack.DTOs.Requests;

import com.jabai.campustrack.Models.Room;

import jakarta.validation.constraints.NotNull;

public class DeleteRoomRequestDto {
    @NotNull (message="Room is required.")
    public final Room room;

    public DeleteRoomRequestDto(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
