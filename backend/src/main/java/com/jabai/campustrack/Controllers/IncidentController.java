package com.jabai.campustrack.Controllers;

import org.springframework.http.ResponseEntity;
import com.jabai.campustrack.DTOs.Requests.IncidentRequestDto;
import com.jabai.campustrack.DTOs.Requests.IncidentStatusRequestDto;
import com.jabai.campustrack.DTOs.Responses.IncidentResponseDto;
import com.jabai.campustrack.Services.IncidentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.security.Principal;
import java.util.List;

/**
 * <p>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</p>
 *
 * <p>JOHN LOUIE: Perform CRUD operations para sa Incidents table</p>
 *
 * <p>Layer structure:</p>
 * <ul>
 *   <li>Controller layer -> DTO (with annotations)</li>
 *   <li>Service layer -> DTO</li>
 *   <li>Repository layer -> Model</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/incidents")
public class IncidentController {
  private final IncidentService incidents;

  public IncidentController(IncidentService incidents) { this.incidents = incidents; }

  @PostMapping
  public ResponseEntity<IncidentResponseDto> create(@Valid @RequestBody IncidentRequestDto request,
                                                   Principal principal) {
    IncidentResponseDto response = incidents.create(request, principal.getName());
    return ResponseEntity.created(URI.create("/api/incidents/" + response.id())).body(response);
  }

  @GetMapping
  public List<IncidentResponseDto> list(Principal principal,
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
    return incidents.list(principal.getName(), page, size);
  }

  @GetMapping("/{id}")
  public IncidentResponseDto get(@PathVariable long id, Principal principal) {
    return incidents.get(id, principal.getName());
  }

  @PutMapping("/{id}")
  public IncidentResponseDto update(@PathVariable long id, @Valid @RequestBody IncidentRequestDto request,
                                    Principal principal) {
    return incidents.update(id, request, principal.getName());
  }

  // Assignment and lifecycle changes use their own validated request.
  @PatchMapping("/{id}/status")
  public IncidentResponseDto updateStatus(@PathVariable long id,
      @Valid @RequestBody IncidentStatusRequestDto request, Principal principal) {
    return incidents.updateStatus(id, request, principal.getName());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable long id, Principal principal) {
    incidents.delete(id, principal.getName());
    return ResponseEntity.noContent().build();
  }
}
