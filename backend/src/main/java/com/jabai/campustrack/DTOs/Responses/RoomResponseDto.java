package com.jabai.campustrack.DTOs.Responses;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomResponseDto {
    private final Long id;
    private final LocalDateTime createdAt;
    private final BuildingResponseDto building;
    private final Long buildingId;
    private final String roomNumber;
    private final Integer capacity;
    private final RoomType roomType;
    private final RoomCriticality criticality;

    public RoomResponseDto(long id, LocalDateTime createdAt, BuildingResponseDto building , String room_number, int capacity, RoomType room_type, RoomCriticality criticality) {
        this.id = id;
        this.createdAt = createdAt;
        this.building = building;
        this.buildingId = null;
        this.roomNumber = room_number;
        this.capacity = capacity;
        this.roomType = room_type;
        this.criticality = criticality;
    }

    public RoomResponseDto(long id, LocalDateTime createdAt, long buildingId, String room_number, int capacity, RoomType room_type, RoomCriticality criticality) {
        this.id = id;
        this.createdAt = createdAt;
        this.building = null;
        this.buildingId = buildingId;
        this.roomNumber = room_number;
        this.capacity = capacity;
        this.roomType = room_type;
        this.criticality = criticality;
    }

    public long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public BuildingResponseDto getBuilding() { return building; }
    public Long getBuildingId() { return buildingId; }
    public String getRoomNumber() { return roomNumber; }
    public Integer getCapacity() { return capacity; }
    public RoomType getRoomType() { return roomType; }
    public RoomCriticality getCriticality() { return criticality; }
}
