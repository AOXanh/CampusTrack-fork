package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.MaintenanceRecordDto;
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
    public MaintenanceRecordDto createRecord(MaintenanceRecordDto dto) {
        return mapToDto(repository.save(mapToModel(dto)));
    }

    // GET ALL (Paginated)
    public Page<MaintenanceRecordDto> getAllRecords(Pageable pageable) {
        return repository.findAll(pageable).map(this::mapToDto);
    }

    // GET ONE
    public MaintenanceRecordDto getRecordById(Long id) {
        return mapToDto(findRecordOrThrow(id));
    }

    // UPDATE
    public MaintenanceRecordDto updateRecord(Long id, MaintenanceRecordDto dto) {
        MaintenanceRecord record = findRecordOrThrow(id);
        
        if (dto.getAction() != null) record.setAction(dto.getAction());
        if (dto.getRemarks() != null) record.setRemarks(dto.getRemarks());
        if (dto.getCompletedAt() != null) record.setCompletedAt(dto.getCompletedAt());
        if (dto.getStatus() != null) record.setStatus(MaintenanceRecordStatus.valueOf(dto.getStatus().toUpperCase()));
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
            .orElseThrow(() -> new RuntimeException("Record not found with id: " + id));
    }

    private MaintenanceRecord mapToModel(MaintenanceRecordDto dto) {
        MaintenanceRecordStatus status = dto.getStatus() != null 
            ? MaintenanceRecordStatus.valueOf(dto.getStatus().toUpperCase()) 
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

    private MaintenanceRecordDto mapToDto(MaintenanceRecord model) {
        MaintenanceRecordDto dto = new MaintenanceRecordDto();
        
        dto.setId(model.getId());
        dto.setAction(model.getAction());
        dto.setRemarks(model.getRemarks());
        dto.setCreatedAt(model.getCreatedAt());
        dto.setCompletedAt(model.getCompletedAt());
        
        if (model.getStatus() != null) dto.setStatus(model.getStatus().name());
        if (model.getIncident() != null) dto.setIncidentId(model.getIncident().getId());
        if (model.getUser() != null) dto.setUserId(model.getUser().getId());
        
        return dto;
    }
}