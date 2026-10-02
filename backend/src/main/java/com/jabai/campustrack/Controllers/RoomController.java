package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.CreateRoomRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateRoomRequestDto;
import com.jabai.campustrack.DTOs.Responses.UpdateRoomResponseDto;
import com.jabai.campustrack.Repositories.RoomJson;
import com.jabai.campustrack.DTOs.Responses.CreateRoomResponseDto;
import com.jabai.campustrack.DTOs.Responses.DeleteRoomResponseDto;
import com.jabai.campustrack.DTOs.Responses.GetRoomResponseDto;
import com.jabai.campustrack.DTOs.Requests.GetRoomsRequestDto;
import com.jabai.campustrack.DTOs.Responses.GetRoomsResponseDto;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jabai.campustrack.Services.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping
  public ResponseEntity<CreateRoomResponseDto> createRoom(@Valid @RequestBody CreateRoomRequestDto createRoomRequestDto) {
    CreateRoomResponseDto responseDto = roomService.createRoom(createRoomRequestDto);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping
  public ResponseEntity<PagedModel<EntityModel<RoomJson>>> getRooms(@Valid GetRoomsRequestDto getRoomsRequestDto, Pageable pageable) {
    PagedModel<EntityModel<RoomJson>> response = roomService.getRooms(getRoomsRequestDto, pageable);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetRoomResponseDto> getRoom(@PathVariable Long id) {
    GetRoomResponseDto responseDto = roomService.getRoom(id);
    return ResponseEntity.ok(responseDto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UpdateRoomResponseDto> updateRoom(@PathVariable Long id, @RequestBody UpdateRoomRequestDto updateRoomRequestDto) {
    UpdateRoomResponseDto responseDto = roomService.updateRoom(id, updateRoomRequestDto);
    return ResponseEntity.ok(responseDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<DeleteRoomResponseDto> deleteRoom(@PathVariable Long id) {
    DeleteRoomResponseDto responseDto = roomService.deleteRoom(id);
    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/hello-world")
  public ResponseEntity<String> helloWorld() {
    return ResponseEntity.ok("Hello world from rooms ;D");
  }
}
