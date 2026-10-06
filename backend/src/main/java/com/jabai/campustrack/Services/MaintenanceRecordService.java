package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Requests.CreateMaintenanceRecordRequestDto;
import com.jabai.campustrack.DTOs.Requests.UpdateMaintenanceRecordRequestDto;
import com.jabai.campustrack.DTOs.Responses.IncidentResponseDto;
import com.jabai.campustrack.DTOs.Responses.MaintenanceRecordResponseDto;
import com.jabai.campustrack.DTOs.Responses.UserProfileResponseDto;
import com.jabai.campustrack.Exceptions.CustomExceptions.RowNotFoundException;
import com.jabai.campustrack.Models.Incident;
import com.jabai.campustrack.Models.MaintenanceRecord;
import com.jabai.campustrack.Models.User;
import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;
import com.jabai.campustrack.Repositories.IncidentRepository;
import com.jabai.campustrack.Repositories.MaintenanceRecordRepository;
import com.jabai.campustrack.Repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MaintenanceRecordService {
    private final MaintenanceRecordRepository maintenanceRecordRepository;
    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public MaintenanceRecordService(MaintenanceRecordRepository maintenanceRecordRepository, IncidentRepository incidentRepository, UserRepository userRepository) {
        this.maintenanceRecordRepository = maintenanceRecordRepository;
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
    }

    // ===== Main operations =====
    // Create
    public MaintenanceRecordResponseDto createRecord(CreateMaintenanceRecordRequestDto dto) {
        Long incidentId = dto.getIncidentId();
        Long userId = dto.getUserId();
        String action = dto.getAction();
        MaintenanceRecordStatus status = dto.getStatus();
        String remarks = dto.getRemarks();
        LocalDateTime completedAt = dto.getCompletedAt();

        Incident foundIncident = incidentRepository
                .findById(incidentId)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find incident with an ID of %d.", incidentId)));
        User foundUser = userRepository
                .findById(userId)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find user with an ID of %d.", userId)));

        MaintenanceRecord newMaintenanceRecord = new MaintenanceRecord(foundIncident, foundUser, action, remarks, status, completedAt);
        MaintenanceRecord response = maintenanceRecordRepository.save(newMaintenanceRecord);

        return buildMaintenanceRecordResponseDto(response);
    }

    // Read all
    public Page<MaintenanceRecordResponseDto> getAllRecords(Pageable pageable) {
        return maintenanceRecordRepository
                .findAll(pageable)
                .map(this::buildMaintenanceRecordResponseDto);
    }

    // Get
    public MaintenanceRecordResponseDto getRecordById(Long id) {
        MaintenanceRecord foundMaintenanceRecord = maintenanceRecordRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find maintenance record with an ID of %d.", id)));
        return buildMaintenanceRecordResponseDto(foundMaintenanceRecord);
    }

    // Update
    public MaintenanceRecordResponseDto updateRecord(Long id, UpdateMaintenanceRecordRequestDto dto) {
        MaintenanceRecord foundMaintenanceRecord = maintenanceRecordRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find maintenance record with an ID of %d.", id)));

        Long incidentId = dto.getIncidentId();
        Long userId = dto.getUserId();
        String action = dto.getAction();
        String remarks = dto.getRemarks();
        MaintenanceRecordStatus status = dto.getStatus();
        LocalDateTime completedAt = dto.getCompletedAt();

        if (incidentId != null) {
            Incident foundIncident = incidentRepository
                    .findById(incidentId)
                    .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find incident with an ID of %d.", incidentId)));
            foundMaintenanceRecord.setIncident(foundIncident);
        }
        if (userId != null) {
            User foundUser = userRepository
                    .findById(userId)
                    .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find user with an ID of %d.", userId)));
            foundMaintenanceRecord.setUser(foundUser);
        }
        if (action != null)
            foundMaintenanceRecord.setAction(action);
        if (remarks != null)
            foundMaintenanceRecord.setRemarks(remarks);
        if (status != null)
            foundMaintenanceRecord.setStatus(status);
        if (completedAt != null)
            foundMaintenanceRecord.setCompletedAt(completedAt);

        MaintenanceRecord response = maintenanceRecordRepository.save(foundMaintenanceRecord);

        return buildMaintenanceRecordResponseDto(response);
    }

    // Delete
    public void deleteRecord(Long id) {
        MaintenanceRecord record = maintenanceRecordRepository
                .findById(id)
                .orElseThrow(() -> new RowNotFoundException(String.format("Unable to find maintenance record with an ID of %d.", id)));

        maintenanceRecordRepository.delete(record);
    }


    // ===== Service utils =====
    private MaintenanceRecordResponseDto buildMaintenanceRecordResponseDto(MaintenanceRecord maintenanceRecord) {
        Incident incident = maintenanceRecord.getIncident();
        User user = maintenanceRecord.getUser();

        UserProfileResponseDto userProfileResponseDto = new UserProfileResponseDto(
                user.getName(),
                user.getEmail(),
                user.getUserRole()
        );

        IncidentResponseDto incidentResponseDto = new IncidentResponseDto(
                incident.getId(),
                incident.getCreatedAt(),
                incident.getEvaluatedAt(),
                incident.getResolvedAt(),
                incident.getClosedAt(),
                incident.getRoom().getId(),
                incident.getReportedBy().getId(),
                incident.getIncidentNumber(),
                incident.getDescription(),
                incident.getSafetyHazard(),
                incident.getOperationalImpact(),
                incident.getPriorityScore(),
                incident.getCategory(),
                incident.getStatus()
        );

        if (incident.getAsset() != null)
            incidentResponseDto.setAssetId(incident.getAsset().getId());
        if (incident.getAssignedTo() != null)
            incidentResponseDto.setAssignedTo(incident.getAssignedTo().getId());

        return new MaintenanceRecordResponseDto(
                maintenanceRecord.getId(),
                incidentResponseDto,
                userProfileResponseDto,
                maintenanceRecord.getAction(),
                maintenanceRecord.getRemarks(),
                maintenanceRecord.getStatus(),
                maintenanceRecord.getCompletedAt(),
                maintenanceRecord.getCreatedAt()
        );
    }
}