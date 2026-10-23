package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.CreateIncidentRequestDto;
import com.jabai.campustrack.DTOs.Requests.SearchIncidentRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateIncidentRequestDto;
import com.jabai.campustrack.DTOs.Responses.IncidentResponseDto;
import com.jabai.campustrack.Services.IncidentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h1>IncidentController: JOHN LOUIE </h1>
 *
 * <p>JADE: Palihug lang ko ug follow up sa mga user roles ari</p>
 */
@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
  private final IncidentService incidentService;

  public IncidentController(IncidentService incidentService) {
    this.incidentService = incidentService;
  }

  // Create
  @PostMapping
  public ResponseEntity<IncidentResponseDto> createIncident(@Valid @RequestBody CreateIncidentRequestDto request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(incidentService.createIncident(request));
  }

  // Read all
  @GetMapping
  public ResponseEntity<Page<IncidentResponseDto>> getIncidents(@PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(incidentService.getIncidents(pageable));
  }

  // Search
  @GetMapping("/search")
  public ResponseEntity<Page<IncidentResponseDto>> searchIncidents(@Valid SearchIncidentRequestDto request, @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(incidentService.searchIncidents(request, pageable));
  }

  // Read
  @GetMapping("/{id}")
  public ResponseEntity<IncidentResponseDto> getIncident(@PathVariable Long id) {
    return ResponseEntity.ok(incidentService.getIncident(id));
  }

  // Update
  @PatchMapping("/{id}")
  public ResponseEntity<IncidentResponseDto> updateIncident(@PathVariable Long id, @Valid @RequestBody UpdateIncidentRequestDto request) {
    return ResponseEntity.ok(incidentService.updateIncident(id, request));
  }

  // Delete
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteIncident(@PathVariable Long id) {
    incidentService.deleteIncident(id);
    return ResponseEntity.noContent().build();
  }
}
