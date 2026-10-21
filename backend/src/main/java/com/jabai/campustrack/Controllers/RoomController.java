package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.CreateRoomRequestDto;
import com.jabai.campustrack.DTOs.Requests.SearchRoomRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateRoomRequestDto;
import com.jabai.campustrack.DTOs.Responses.RoomResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.jabai.campustrack.Services.RoomService;

import jakarta.validation.Valid;

/**
 * <h1>RoomController: CHARLES</h1>
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  // Create
  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping
  public ResponseEntity<RoomResponseDto> createRoom(@Valid @RequestBody CreateRoomRequestDto createRoomRequestDto) {
    RoomResponseDto responseDto = roomService.createRoom(createRoomRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  // Read all
  @GetMapping
  public ResponseEntity<Page<RoomResponseDto>> getRooms(@PageableDefault(size = 20) Pageable pageable) {
    Page<RoomResponseDto> responseDto = roomService.getRooms(pageable);
    return ResponseEntity.ok(responseDto);
  }

  // Search
  @GetMapping("/search")
  public ResponseEntity<Page<RoomResponseDto>> searchRooms(
          SearchRoomRequestDto searchRoomsRequestDto,
          @PageableDefault(size = 20) Pageable pageable
  ) {
    Page<RoomResponseDto> responseDto = roomService.searchRooms(searchRoomsRequestDto, pageable);
    return ResponseEntity.ok(responseDto);
  }

  // Read
  @GetMapping("/{id}")
  public ResponseEntity<RoomResponseDto> getRoom(@PathVariable Long id) {
    RoomResponseDto responseDto = roomService.getRoom(id);
    return ResponseEntity.ok(responseDto);
  }

  // Update
  @PreAuthorize("hasRole('ADMIN')")
  @PatchMapping("/{id}")
  public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long id, @RequestBody UpdateRoomRequestDto updateRoomRequestDto) {
    RoomResponseDto responseDto = roomService.updateRoom(id, updateRoomRequestDto);
    return ResponseEntity.ok(responseDto);
  }

  // Delete
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/{id}")
  public ResponseEntity<RoomResponseDto> deleteRoom(@PathVariable Long id) {
    roomService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
