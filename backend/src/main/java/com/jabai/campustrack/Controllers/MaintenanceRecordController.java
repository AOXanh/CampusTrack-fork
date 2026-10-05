package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.MaintenanceRecordDto;
import com.jabai.campustrack.Services.MaintenanceRecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h4>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</h4>
 * <br/>
 * <p>CJ: Perform CRUD operations para sa Maintenance Records table</p>
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

    @Autowired
    private MaintenanceRecordService service;

    @GetMapping("/hello-world")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok("Hello world from maintenance records ;D");
    }

    // CREATE -> POST (with body)
    @PostMapping
    public ResponseEntity<MaintenanceRecordDto> createRecord(@Valid @RequestBody MaintenanceRecordDto dto) {
        MaintenanceRecordDto created = service.createRecord(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // GET ALL -> GET (with optional pagination parameters)
    @GetMapping
    public ResponseEntity<Page<MaintenanceRecordDto>> getAllRecords(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<MaintenanceRecordDto> records = service.getAllRecords(pageable);
        return ResponseEntity.ok(records);
    }

    // GET ONE -> GET (by ID)
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecordDto> getRecordById(@PathVariable Long id) {
        MaintenanceRecordDto record = service.getRecordById(id);
        return ResponseEntity.ok(record);
    }

    // UPDATE -> PUT (with optional body)
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceRecordDto> updateRecord(
            @PathVariable Long id, 
            @RequestBody MaintenanceRecordDto dto) {
        
        MaintenanceRecordDto updated = service.updateRecord(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE -> DELETE (by ID)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        service.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}