package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.CreateRoomRequestDto;
import com.jabai.campustrack.DTOs.Requests.RegisterUserRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateRoomRequestDto;
import com.jabai.campustrack.DTOs.Responses.UpdateRoomResponseDto;
import com.jabai.campustrack.DTOs.Responses.CreateRoomResponseDto;
import com.jabai.campustrack.DTOs.Requests.GetRoomsRequestDto;
import com.jabai.campustrack.DTOs.Responses.GetRoomsResponseDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jabai.campustrack.Services.RoomService;

import jakarta.validation.Valid;

/**
 * <p>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</p>
 *
 * <p>KYLE: Perform CRUD operations para sa Rooms table</p>
 *
 * <p>Layer structure:</p>
 * <ul>
 *   <li>Controller layer -> DTO (with annotations)</li>
 *   <li>Service layer -> DTO</li>
 *   <li>Repository layer -> Model</li>
 * </ul>
 * 
 * Just waiting for Building's Controller layer/Service layer/Repository layer to be done to able to
 * do checks if building specified exists in the database.
 * 
 * do MaintenanceRecordController.java first before doing RoomController.java
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping("/create-room")
  public ResponseEntity<CreateRoomResponseDto> createRoom(@Valid @RequestBody CreateRoomRequestDto createRoomRequestDto) {
    CreateRoomResponseDto responseDto = roomService.createRoom(createRoomRequestDto);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/get-rooms")
  public ResponseEntity<GetRoomsResponseDto> getRooms(@Valid GetRoomsRequestDto getRoomsRequestDto) {
    GetRoomsResponseDto responseDto = roomService.getRooms(getRoomsRequestDto);
    return ResponseEntity.ok(responseDto);
  }

  // update room not tested yet, please test!
  @PatchMapping("/update-room")
  public ResponseEntity<UpdateRoomResponseDto> updateRoom(UpdateRoomRequestDto updateRoomRequestDto) {
    UpdateRoomResponseDto responseDto = roomService.updateRoom(updateRoomRequestDto);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/hello-world")
  public ResponseEntity<String> helloWorld() {
    return ResponseEntity.ok("Hello world from rooms ;D");
  }
}
