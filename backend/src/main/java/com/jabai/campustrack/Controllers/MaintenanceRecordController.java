package com.jabai.campustrack.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <h4>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</h4>
 * <br/>
 * <p>KYLE: Perform CRUD operations para sa Maintenance Records table</p>
 * <br/>
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
  @GetMapping("/hello-world")
  public ResponseEntity<String> helloWorld() {
    return ResponseEntity.ok("Hello world from maintenance records ;D");
  }
}
