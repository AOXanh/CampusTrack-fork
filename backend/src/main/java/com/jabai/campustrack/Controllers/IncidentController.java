package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.CreateIncidentRequestDto;
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
 * <h4>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</h4>
 * <br/>
 * <p>JOHN LOUIE: Perform search by:</p>
 * <ul>
 *   <li>from (optional) -> Dapat naka format ha for example 2026-10-07 (CREATED AT)</li>
 *   <li>to (optional) -> Dapat naka format ha for example 2026-10-07 (CREATED AT)</li>
 *   <li>assetId (optional)</li>
 *   <li>roomId (optional)</li>
 *   <li>reportedById (optional)</li>
 *   <li>assignedToId (optional)</li>
 *   <li>incidentNumber (optional)</li>
 *   <li>description (optional) -> Dapat naka keyword ni</li>
 *   <li>safetyHazard (optional)</li>
 *   <li>operationalImpact (optional)</li>
 *   <li>affectedArea (optional)</li>
 *   <li>priorityScore (optional)</li>
 *   <li>category (optional) (DAPAT NAKA ENUMS)</li>
 *   <li>priority (optional) (DAPAT NAKA ENUMS)</li>
 *   <li>status (optional) (DAPAT NAKA ENUMS)</li>
 * </ul>
 * <p>P.S.: And dapat naka paginate gihapon sya.</p>
 * <p>Expected URL: <code>/api/incidents/search?from=2026-10-07&to=2026-10-08&assetId=1&roomId=1&reportedById=1&assignedToId=2&incidentNumber=INC-0001&description=broken+projector&safetyHazard=true&operationalImpact=HIGH&affectedArea=Lab+Room&priorityScore=80&category=EQUIPMENT_FAILURE&priority=HIGH&status=OPEN</code></p>
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
