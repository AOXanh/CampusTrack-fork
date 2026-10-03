package com.jabai.campustrack.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jabai.campustrack.DTOs.Responses.MaintenanceRecordResponseDto;

/**
 * <p>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</p>
 *
 * <p>KYLE: Perform CRUD operations para sa Maintenance Records table</p>
 *
 * <p>Layer structure:</p>
 * <ul>
 *   <li>Controller layer -> DTO (with annotations)</li>
 *   <li>Service layer -> DTO</li>
 *   <li>Repository layer -> Model</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {

  @PostMapping
  public ResponseEntity<MaintenanceRecordResponseDto> createRecord() {
    
    return ResponseEntity.ok(new MaintenanceRecordResponseDto()); // to stop the error, ill do this later
  }

  @GetMapping("/hello-world")
  public ResponseEntity<String> helloWorld() {
    return ResponseEntity.ok("Hello world from maintenance records ;D");
  }
}
