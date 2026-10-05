package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Requests.CreateMaintenanceRecordRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateMaintenanceRecordRequestDto;
import com.jabai.campustrack.DTOs.Responses.MaintenanceRecordResponseDto;
import com.jabai.campustrack.Exceptions.CustomExceptions.RowNotFoundException;
import com.jabai.campustrack.Models.Incident;
import com.jabai.campustrack.Models.MaintenanceRecord;
import com.jabai.campustrack.Models.User;
import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;
import com.jabai.campustrack.Repositories.MaintenanceRecordRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MaintenanceRecordService {

    @Autowired
    private MaintenanceRecordRepository repository;

    // Use EntityManager to bypass protected constructors without needing repositories
    @Autowired
    private EntityManager entityManager;

    // CREATE
    public MaintenanceRecordResponseDto createRecord(CreateMaintenanceRecordRequestDto dto) {
        return mapToDto(repository.save(mapToModel(dto)));
    }

    // GET ALL (Paginated)
    public Page<MaintenanceRecordResponseDto> getAllRecords(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToDto);
    }

    // GET ONE
    public MaintenanceRecordResponseDto getRecordById(Long id) {
        return mapToDto(findRecordOrThrow(id));
    }

    // UPDATE
    public MaintenanceRecordResponseDto updateRecord(Long id, UpdateMaintenanceRecordRequestDto dto) {
        MaintenanceRecord record = findRecordOrThrow(id);
        
        if (dto.getAction() != null) record.setAction(dto.getAction());
        if (dto.getRemarks() != null) record.setRemarks(dto.getRemarks());
        if (dto.getCompletedAt() != null) record.setCompletedAt(dto.getCompletedAt());
        if (dto.getStatus() != null) record.setStatus(MaintenanceRecordStatus.valueOf(dto.getStatus()));
        
        // Safely link relationships using EntityManager
        if (dto.getIncidentId() != null) record.setIncident(entityManager.getReference(Incident.class, dto.getIncidentId()));
        if (dto.getUserId() != null) record.setUser(entityManager.getReference(User.class, dto.getUserId()));
        
        return mapToDto(repository.save(record));
    }

    // DELETE
    public void deleteRecord(Long id) {
        repository.delete(findRecordOrThrow(id));
    }

    // ==========================================
    // PRIVATE HELPER & MAPPER METHODS
    // ==========================================

    private MaintenanceRecord findRecordOrThrow(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RowNotFoundException("Record not found with id: " + id));
    }

    private MaintenanceRecord mapToModel(CreateMaintenanceRecordRequestDto dto) {
        MaintenanceRecordStatus status = dto.getStatus() != null 
            ? MaintenanceRecordStatus.valueOf(dto.getStatus()) 
            : MaintenanceRecordStatus.PENDING; 

        // entityManager.getReference() safely gets the Entity by ID without triggering a database query
        MaintenanceRecord model = new MaintenanceRecord(
            entityManager.getReference(Incident.class, dto.getIncidentId()),
            entityManager.getReference(User.class, dto.getUserId()),
            dto.getAction(),
            dto.getRemarks(),
            status
        );
        
        model.setCompletedAt(dto.getCompletedAt());
        return model;
    }

    private MaintenanceRecordResponseDto mapToDto(MaintenanceRecord model) {
        MaintenanceRecordResponseDto dto = new MaintenanceRecordResponseDto();
        
        dto.setId(model.getId());
        dto.setAction(model.getAction());
        dto.setRemarks(model.getRemarks());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setCompletedAt(model.getCompletedAt());
        
        if (model.getStatus() != null) dto.setStatus(model.getStatus());
        if (model.getIncident() != null) dto.setIncidentId(model.getIncident().getId());
        if (model.getUser() != null) dto.setUserId(model.getUser().getId());
        
        return dto;
    }
}