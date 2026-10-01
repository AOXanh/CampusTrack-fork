package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Responses.CreateRoomResponseDto;
import com.jabai.campustrack.Models.Room;
import com.jabai.campustrack.Models.Building;
import com.jabai.campustrack.Repositories.RoomRepository;
import com.jabai.campustrack.Repositories.RoomJson;
import com.jabai.campustrack.DTOs.Requests.CreateRoomRequestDto;
import com.jabai.campustrack.DTOs.Responses.GetRoomsResponseDto;
import com.jabai.campustrack.DTOs.Responses.UpdateRoomResponseDto;
import com.jabai.campustrack.DTOs.Requests.GetRoomsRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateRoomRequestDto;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service 
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    } // g

    public CreateRoomResponseDto createRoom(CreateRoomRequestDto createRoomRequestDto) {
        // Building building = buildingRepository.findByBuilding(); if building repository exists use this to check if building actually exists (validates both id and name)
        Room room = new Room(
            createRoomRequestDto.getBuilding(),
            createRoomRequestDto.getRoomnumber(),
            createRoomRequestDto.getCapacity(),
            createRoomRequestDto.getRoomtype(),
            createRoomRequestDto.getCriticality()
        );

        /* 
        this is how a building json looks like
            {
            "name": "Room 101",
            "building": { "id": 7 }
            }
        */ 
        // if it exists, it will add the room, if not, it will throw an error
        // i would preferrably add a user facing error, but for now, since there's no
        // buildingRepository to do the check, this will do. 
        roomRepository.save(room);
        // wont work until we can do a check if building actually exists (and get its id).
        return new CreateRoomResponseDto("Successfully created the room!");
    }

    public GetRoomsResponseDto getRooms(GetRoomsRequestDto getRoomsRequestDto) {
        Long buildingID = getRoomsRequestDto.getBuildingID();
        List<RoomJson> fetchedRooms = roomRepository.findByBuilding_Id(buildingID);

        return new GetRoomsResponseDto(fetchedRooms);
    }
    
    public UpdateRoomResponseDto updateRoom(UpdateRoomRequestDto updateRoomRequestDto) {
        Room Updatedroom = updateRoomRequestDto.getRoom();
        Room Savedroom = roomRepository.findById(Updatedroom.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found."));

        if (Updatedroom.getBuilding() != null) {Savedroom.setBuilding(Updatedroom.getBuilding());}
        if (Updatedroom.getCapacity() > 0) {Savedroom.setCapacity(Updatedroom.getCapacity());}
        if (Updatedroom.getCriticality() != null) {Savedroom.setCriticality(Updatedroom.getCriticality());}
        if (Updatedroom.getRoomType() != null) {Savedroom.setRoomType(Updatedroom.getRoomType());}
        // idk if room number should be editable

        roomRepository.save(Savedroom);
        return new UpdateRoomResponseDto("Successfully updated room.");
    }
}
