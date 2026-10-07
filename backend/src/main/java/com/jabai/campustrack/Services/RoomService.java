package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Responses.BuildingResponseDto;
import com.jabai.campustrack.DTOs.Responses.RoomResponseDto;
import com.jabai.campustrack.Exceptions.CustomExceptions.RowNotFoundException;
import com.jabai.campustrack.Models.Building;
import com.jabai.campustrack.Models.Enums.RoomCriticality;
import com.jabai.campustrack.Models.Enums.RoomType;
import com.jabai.campustrack.Models.Room;
import com.jabai.campustrack.Repositories.RoomRepository;
import com.jabai.campustrack.Repositories.Specifications.RoomSpecifications;
import com.jabai.campustrack.Repositories.BuildingRepository;
import com.jabai.campustrack.DTOs.Requests.CreateRoomRequestDto;
import com.jabai.campustrack.DTOs.Requests.SearchRoomsRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateRoomRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service 
public class RoomService {
    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;

    public RoomService(RoomRepository roomRepository, BuildingRepository buildingRepository) {
        this.roomRepository = roomRepository;
        this.buildingRepository = buildingRepository;
    }

    // ===== Main operations =====
    // Create
    public RoomResponseDto createRoom(CreateRoomRequestDto createRoomRequestDto) {
        Long buildingId = createRoomRequestDto.getBuildingId();
        String roomNumber = createRoomRequestDto.getRoomNumber();
        Integer capacity = createRoomRequestDto.getCapacity();
        RoomType roomType = createRoomRequestDto.getRoomType();
        RoomCriticality criticality = createRoomRequestDto.getCriticality();

        Building foundBuilding = buildingRepository
                .findById(buildingId)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find building with an ID of %d", createRoomRequestDto.getBuildingId())));
        Room room = new Room(foundBuilding, roomNumber, capacity, roomType, criticality);
        roomRepository.save(room);

        return buildRoomResponseDto(room);
    }

    // Read all
    public Page<RoomResponseDto> getRooms(Pageable pageable) {
        return roomRepository
                .findAll(pageable)
                .map(this::buildRoomResponseDto);
    }

    public Page<RoomResponseDto> searchRooms(SearchRoomsRequestDto searchRoomsRequestDto, Pageable pageable) {
        Specification<Room> spec = Specification.where(
            RoomSpecifications.hasBuilding(searchRoomsRequestDto.building_id())
        ).and(RoomSpecifications.hasCapacity(searchRoomsRequestDto.capacity()))
        .and(RoomSpecifications.hasCriticality(searchRoomsRequestDto.criticality()))
        .and(RoomSpecifications.hasRoomNumber(searchRoomsRequestDto.room_number()))
        .and(RoomSpecifications.hasRoomType(searchRoomsRequestDto.room_type()));

        return roomRepository.findAll(spec, pageable)
            .map(this::buildRoomResponseDto);
    }

    // Read
    public RoomResponseDto getRoom(long roomId) {
        Room fetchedRoom = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find room with an ID of %d", roomId)));

        return buildRoomResponseDto(fetchedRoom);
    }

    // Update
    public RoomResponseDto updateRoom(long roomId, UpdateRoomRequestDto updateRoomRequestDto) {
        Long buildingId = updateRoomRequestDto.getBuildingId();
        String roomNumber = updateRoomRequestDto.getRoomNumber();
        Integer capacity = updateRoomRequestDto.getCapacity();
        RoomType roomType = updateRoomRequestDto.getRoomType();
        RoomCriticality criticality = updateRoomRequestDto.getCriticality();

        Room Savedroom = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find room with an ID of %d", roomId)));

        if (buildingId != null) {
            Building building = buildingRepository
                    .findById(buildingId)
                    .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find building with an ID of %d", buildingId)));
            Savedroom.setBuilding(building);
        }
        if (roomNumber != null)
            Savedroom.setRoomNumber(roomNumber);
        if (capacity != null)
            Savedroom.setCapacity(capacity);
        if (roomType != null)
            Savedroom.setRoomType(roomType);
        if (criticality != null)
            Savedroom.setCriticality(criticality);
        
        roomRepository.save(Savedroom);
        return buildRoomResponseDto(Savedroom);
    }

    // Delete
    public void delete(long roomId) {
        Room SavedRoom = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found."));

        roomRepository.delete(SavedRoom);
    }

    // ===== Service Utils =====
    private RoomResponseDto buildRoomResponseDto(Room room) {
        BuildingResponseDto buildingResponseDto = new BuildingResponseDto(
                room.getBuilding().getId(),
                room.getBuilding().getName(),
                room.getBuilding().getCreatedAt()
        );

        return new RoomResponseDto(
            room.getId(), 
            room.getCreatedAt(),
            buildingResponseDto,
            room.getRoomNumber(),
            room.getCapacity(), 
            room.getRoomType(), 
            room.getCriticality()
        );
    }
}
