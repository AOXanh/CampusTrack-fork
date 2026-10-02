package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Responses.CreateRoomResponseDto;
import com.jabai.campustrack.DTOs.Responses.DeleteRoomResponseDto;
import com.jabai.campustrack.DTOs.Responses.GetRoomResponseDto;
import com.jabai.campustrack.Models.Room;
import com.jabai.campustrack.Models.Building;
import com.jabai.campustrack.Repositories.RoomRepository;
import com.jabai.campustrack.Repositories.RoomJson;
import com.jabai.campustrack.DTOs.Requests.CreateRoomRequestDto;
import com.jabai.campustrack.DTOs.Responses.UpdateRoomResponseDto;
import com.jabai.campustrack.DTOs.Requests.GetRoomsRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateRoomRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;
import java.util.Map;

@Service 
public class RoomService {
    private final RoomRepository roomRepository;
    private final PagedResourcesAssembler<RoomJson> assembler;

    public RoomService(RoomRepository roomRepository, PagedResourcesAssembler<RoomJson> assembler) {
        this.roomRepository = roomRepository;
        this.assembler = assembler;
    }

    public CreateRoomResponseDto createRoom(CreateRoomRequestDto createRoomRequestDto) {
        try {
            Integer.parseInt(createRoomRequestDto.getRoomNumber());
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Room number should be an integer.");
        }
        // Building building = buildingRepository.findByBuilding(); if building repository exists use this to check if building actually exists (validates both id and name)
        Room room = new Room(
            createRoomRequestDto.getBuilding(),
            createRoomRequestDto.getRoomNumber(),
            createRoomRequestDto.getCapacity(),
            createRoomRequestDto.getRoomType(),
            createRoomRequestDto.getCriticality()
        );
    
        // if it exists, it will add the room, if not, it will throw an error
        // i would preferrably add a user facing error, but for now, since there's no
        // buildingRepository to do the check, this will do. 
        roomRepository.save(room);
        return new CreateRoomResponseDto("Successfully created the room!");
    }

    public PagedModel<EntityModel<RoomJson>> getRooms(GetRoomsRequestDto getRoomsRequestDto, Pageable pageable) {
        Long buildingID = getRoomsRequestDto.getBuilding_id();
        Page<RoomJson> fetchedRooms = roomRepository.search(buildingID, getRoomsRequestDto.getCriticality(), getRoomsRequestDto.getRoomType(), pageable);
        
        return assembler.toModel(fetchedRooms);
    }

    public GetRoomResponseDto getRoom(long roomId) {
        Room fetchedRoom = roomRepository.findById(roomId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found."));

        Building roomBuilding = fetchedRoom.getBuilding();
        Map<String, Object> buildingJson = new HashMap<>();

        buildingJson.put("id", roomBuilding.getId());
        buildingJson.put("createdAt", roomBuilding.getCreatedAt());
        buildingJson.put("name", roomBuilding.getName());

        return new GetRoomResponseDto(
            fetchedRoom.getId(), 
            fetchedRoom.getCreatedAt(), 
            buildingJson,
            fetchedRoom.getRoomNumber(),
            fetchedRoom.getCapacity(),
            fetchedRoom.getRoomType(),
            fetchedRoom.getCriticality()
        );
    }
    
    public UpdateRoomResponseDto updateRoom(long roomId, UpdateRoomRequestDto updateRoomRequestDto) {
        //Building building = buildingRepository.findByBuilding(); for check if building exists
        Room Updatedroom = updateRoomRequestDto.getRoom();
        Room Savedroom = roomRepository.findById(roomId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found."));

        if (Updatedroom.getBuilding() != null) {Savedroom.setBuilding(Updatedroom.getBuilding());}
        if (Updatedroom.getCapacity() > 0) {Savedroom.setCapacity(Updatedroom.getCapacity());}
        if (Updatedroom.getCriticality() != null) {Savedroom.setCriticality(Updatedroom.getCriticality());}
        if (Updatedroom.getRoomType() != null) {Savedroom.setRoomType(Updatedroom.getRoomType());}
        if (Updatedroom.getRoomNumber() != null) {Savedroom.setRoomNumber(Updatedroom.getRoomNumber());}
        

        roomRepository.save(Savedroom);
        return new UpdateRoomResponseDto("Successfully updated room.");
    }

    public DeleteRoomResponseDto deleteRoom(long roomId) {
        Room SavedRoom = roomRepository.findById(roomId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found."));
        
        roomRepository.delete(SavedRoom);
        return new DeleteRoomResponseDto("Successfully deleted room.");
    }
}
