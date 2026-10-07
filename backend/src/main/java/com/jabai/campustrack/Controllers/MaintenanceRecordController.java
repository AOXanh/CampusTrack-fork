package com.jabai.campustrack.Controllers;

import com.jabai.campustrack.DTOs.Requests.CreateMaintenanceRecordRequestDto;
import com.jabai.campustrack.DTOs.Requests.SearchMaintenanceRecordRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateMaintenanceRecordRequestDto;
import com.jabai.campustrack.DTOs.Responses.MaintenanceRecordResponseDto;
import com.jabai.campustrack.Services.MaintenanceRecordService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <h4>NOTE: DO NOT VIOLATE LAYERS STRUCTURE.</h4>
 *
 * <p>CJ: Perform search by:</p>
 * <ul>
 *   <li>Incident id (optional)</li>
 *   <li>User id (optional)</li>
 *   <li>Action (optional) -> Dapat naka keyword</li>
 *   <li>Remarks (optional) -> Dapat naka keyword</li>
 *   <li>Status (optional)</li>
 *   <li>Start date (optional) -> Dapat naka format ha for example 2026-10-07 (COMPLETED AT)</li>
 *   <li>End date (optional) -> Dapat naka format ha for example 2026-10-15 (COMPLETED AT)</li>
 * </ul>
 * <p>P.S.: And dapat naka paginate gihapon sya.</p>
 * <p>Expected URL: <code>/api/maintenance-records/search?incident_id=1&user_id=1&action=Nasakpan&remarks=gipa+principal&status=COMPLETED&start_date=2026-10-07&end_date=2026-10-15</code></p>
 */
@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {
    private final MaintenanceRecordService maintenanceRecordService;

    public MaintenanceRecordController(MaintenanceRecordService maintenanceRecordService) {
        this.maintenanceRecordService = maintenanceRecordService;
    }

    // Create
    @PostMapping
    public ResponseEntity<MaintenanceRecordResponseDto> createRecord(@Valid @RequestBody CreateMaintenanceRecordRequestDto dto) {
        MaintenanceRecordResponseDto created = maintenanceRecordService.createRecord(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Read all
    @GetMapping
    public ResponseEntity<Page<MaintenanceRecordResponseDto>> getAllRecords(@PageableDefault(size = 20) Pageable pageable) {
        Page<MaintenanceRecordResponseDto> records = maintenanceRecordService.getAllRecords(pageable);
        return ResponseEntity.ok(records);
    }

    // Search endpoint
    @GetMapping("/search")
    public ResponseEntity<Page<MaintenanceRecordResponseDto>> searchRecords(
            SearchMaintenanceRecordRequestDto searchMaintenanceRecordRequestDto,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<MaintenanceRecordResponseDto> records = maintenanceRecordService.searchRecords(searchMaintenanceRecordRequestDto, pageable);
        return ResponseEntity.ok(records);
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponseDto> getRecordById(@PathVariable Long id) {
        MaintenanceRecordResponseDto record = maintenanceRecordService.getRecordById(id);
        return ResponseEntity.ok(record);
    }

    // Update
    @PatchMapping("/{id}")
    public ResponseEntity<MaintenanceRecordResponseDto> updateRecord(@PathVariable Long id, @RequestBody UpdateMaintenanceRecordRequestDto dto) {
        MaintenanceRecordResponseDto updated = maintenanceRecordService.updateRecord(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id) {
        maintenanceRecordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }
}