package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Responses.CreateRoomResponseDto;
import com.jabai.campustrack.DTOs.Responses.DeleteRoomResponseDto;
import com.jabai.campustrack.Models.Room;
import com.jabai.campustrack.Models.Building;
import com.jabai.campustrack.Repositories.RoomRepository;
import com.jabai.campustrack.Repositories.RoomJson;
import com.jabai.campustrack.DTOs.Requests.CreateRoomRequestDto;
import com.jabai.campustrack.DTOs.Requests.DeleteRoomRequestDto;
import com.jabai.campustrack.DTOs.Responses.GetRoomsResponseDto;
import com.jabai.campustrack.DTOs.Responses.UpdateRoomResponseDto;
import com.jabai.campustrack.DTOs.Requests.GetRoomsRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateRoomRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service 
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public CreateRoomResponseDto createRoom(CreateRoomRequestDto createRoomRequestDto) {
        try {
            Integer.parseInt(createRoomRequestDto.getRoomnumber());
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room number should be an integer.");
        }
        // Building building = buildingRepository.findByBuilding(); if building repository exists use this to check if building actually exists (validates both id and name)
        Room room = new Room(
            createRoomRequestDto.getBuilding(),
            createRoomRequestDto.getRoomnumber(),
            createRoomRequestDto.getCapacity(),
            createRoomRequestDto.getRoomtype(),
            createRoomRequestDto.getCriticality()
        );

        // if it exists, it will add the room, if not, it will throw an error
        // i would preferrably add a user facing error, but for now, since there's no
        // buildingRepository to do the check, this will do. 
        roomRepository.save(room);
        return new CreateRoomResponseDto("Successfully created the room!");
    }

    public GetRoomsResponseDto getRooms(GetRoomsRequestDto getRoomsRequestDto, Pageable pageable) {
        Long buildingID = getRoomsRequestDto.getBuildingID();
        Page<RoomJson> fetchedRooms = roomRepository.search(buildingID, getRoomsRequestDto.getCriticality(), getRoomsRequestDto.getRoomType(), pageable);

        return new GetRoomsResponseDto(fetchedRooms);
    }
    
    public UpdateRoomResponseDto updateRoom(UpdateRoomRequestDto updateRoomRequestDto) {
        Room Updatedroom = updateRoomRequestDto.getRoom();
        //Building building = buildingRepository.findByBuilding(); for check if building exists
        Room Savedroom = roomRepository.findById(Updatedroom.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found."));

        if (Updatedroom.getBuilding() != null) {Savedroom.setBuilding(Updatedroom.getBuilding());}
        if (Updatedroom.getCapacity() > 0) {Savedroom.setCapacity(Updatedroom.getCapacity());}
        if (Updatedroom.getCriticality() != null) {Savedroom.setCriticality(Updatedroom.getCriticality());}
        if (Updatedroom.getRoomType() != null) {Savedroom.setRoomType(Updatedroom.getRoomType());}
        if (Updatedroom.getRoomNumber() != null) {Savedroom.setRoomNumber(Updatedroom.getRoomNumber());}
        

        roomRepository.save(Savedroom);
        return new UpdateRoomResponseDto("Successfully updated room.");
    }

    public DeleteRoomResponseDto deleteRoom(DeleteRoomRequestDto deleteRoomRequestDto) {
        Room Givenroom = deleteRoomRequestDto.getRoom();
        Room SavedRoom = roomRepository.findById(Givenroom.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found."));
        
        roomRepository.delete(SavedRoom);
        return new DeleteRoomResponseDto("Successfully deleted room.");
    }
}
