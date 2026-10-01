package com.jabai.campustrack.DTOs.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;
import com.jabai.campustrack.Models.Building;

public class CreateRoomRequestDto {
    @NotNull(message="Building is required.")
    private final Building building; 

    @NotEmpty(message="Room number is required.") // check if positive and if it is a number
    private final String roomnumber;

    @NotNull(message="Capacity is required.")
    @Positive 
    private final int capacity;

    @NotNull(message="Room Type is required.")
    private final RoomType roomtype;

    @NotNull(message="Criticality is required.")
    private final RoomCriticality criticality;
    
    public CreateRoomRequestDto(Building building, String roomnumber, int capacity, RoomType roomtype, RoomCriticality criticality) {
        this.building = building;
        this.roomnumber = roomnumber;
        this.capacity = capacity;
        this.roomtype = roomtype;
        this.criticality = criticality;
    }

    public Building getBuilding() {
        return building;
    }
    public int getCapacity() {
        return capacity;
    }
    public RoomCriticality getCriticality() {
        return criticality;
    }
    public String getRoomnumber() {
        return roomnumber;
    }
    public RoomType getRoomtype() {
        return roomtype;
    }

    // note for self, do check the input for criticality, roomtype, and building is inside enum and maybe make
    // a message output if it isn't inside it (instead of an error on the console)
}
