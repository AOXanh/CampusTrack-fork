package com.jabai.campustrack.Services;

import com.jabai.campustrack.DTOs.Requests.IncidentRequestDto;
import com.jabai.campustrack.DTOs.Requests.IncidentStatusRequestDto;
import com.jabai.campustrack.DTOs.Responses.IncidentResponseDto;
import com.jabai.campustrack.Models.*;
import com.jabai.campustrack.Models.Enums.*;
import com.jabai.campustrack.Repositories.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
public class IncidentService {
    private final IncidentRepository incidents;
    private final RoomRepository rooms;
    private final AssetRepository assets;
    private final UserRepository users;

    public IncidentService(IncidentRepository incidents, RoomRepository rooms,
                           AssetRepository assets, UserRepository users) {
        this.incidents = incidents;
        this.rooms = rooms;
        this.assets = assets;
        this.users = users;
    }

    public IncidentResponseDto create(IncidentRequestDto request, String email) {
        User reporter = user(email);
        Incident incident = new Incident(null, null, reporter, null,
            "INC-" + UUID.randomUUID(), request.description().trim(),
            request.safetyHazard(), request.operationalImpact(), request.affectedArea(),
            0, request.category(), IncidentPriority.LOW, IncidentStatus.REPORTED);
        applyReport(incident, request);
        return response(incidents.saveAndFlush(incident));
    }

    @Transactional(readOnly = true)
    public List<IncidentResponseDto> list(String email, int page, int size) {
        User actor = user(email);
        if (page < 0 || size < 1 || size > 100)
            throw new ResponseStatusException(BAD_REQUEST, "Page must be nonnegative and size between 1 and 100.");
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt", "id"));
        List<Incident> visible = switch (actor.getUserRole()) {
            case ADMIN -> incidents.findAllBy(pageable);
            case WORKER -> incidents.findByReportedByIdOrAssignedToId(actor.getId(), actor.getId(), pageable);
            case USER -> incidents.findByReportedById(actor.getId(), pageable);
        };
        return visible.stream().map(IncidentService::response).toList();
    }

    @Transactional(readOnly = true)
    public IncidentResponseDto get(long id, String email) {
        User actor = user(email);
        Incident incident = incidents.findById(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Incident not found."));
        // Reporters see their own reports; workers also see incidents assigned to them.
        if (actor.getUserRole() != UserRole.ADMIN
            && incident.getReportedBy().getId() != actor.getId()
            && !(actor.getUserRole() == UserRole.WORKER && isAssigned(incident, actor)))
            throw new ResponseStatusException(FORBIDDEN, "You cannot view this incident.");
        return response(incident);
    }

    public IncidentResponseDto update(long id, IncidentRequestDto request, String email) {
        requireAdmin(user(email));
        Incident incident = lockedIncident(id);
        requireUnworked(incident);
        applyReport(incident, request);
        return response(incidents.saveAndFlush(incident));
    }

    public void delete(long id, String email) {
        requireAdmin(user(email));
        Incident incident = lockedIncident(id);
        requireUnworked(incident);
        incidents.delete(incident);
    }

    public IncidentResponseDto updateStatus(long id, IncidentStatusRequestDto request, String email) {
        User actor = user(email);
        Incident incident = lockedIncident(id);
        IncidentStatus target = request.status();
        if (actor.getUserRole() != UserRole.ADMIN
            && !(actor.getUserRole() == UserRole.WORKER && isAssigned(incident, actor)
                && (target == IncidentStatus.IN_PROGRESS || target == IncidentStatus.RESOLVED)))
            throw new ResponseStatusException(FORBIDDEN, "You cannot change this incident's status.");

        // Keep the documented lifecycle in order; timestamps come from the server.
        IncidentStatus next = switch (incident.getStatus()) {
            case REPORTED -> IncidentStatus.EVALUATED;
            case EVALUATED -> IncidentStatus.ASSIGNED;
            case ASSIGNED -> IncidentStatus.IN_PROGRESS;
            case IN_PROGRESS -> IncidentStatus.RESOLVED;
            case RESOLVED -> IncidentStatus.VERIFIED;
            case VERIFIED -> IncidentStatus.CLOSED;
            case CLOSED -> null;
        };
        if (target != next)
            throw new ResponseStatusException(CONFLICT, "Status must follow the incident lifecycle.");

        if (target == IncidentStatus.ASSIGNED) {
            if (request.assignedToId() == null)
                throw new ResponseStatusException(BAD_REQUEST, "Assign a worker when setting ASSIGNED.");
            User worker = users.findById(request.assignedToId())
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Assigned user not found."));
            if (worker.getUserRole() != UserRole.WORKER)
                throw new ResponseStatusException(BAD_REQUEST, "The assigned user must be a worker.");
            incident.setAssignedTo(worker);
        } else if (request.assignedToId() != null) {
            throw new ResponseStatusException(BAD_REQUEST, "Assignment is only allowed when setting ASSIGNED.");
        }

        if (target == IncidentStatus.EVALUATED) evaluate(incident);
        if (target == IncidentStatus.RESOLVED) incident.setResolvedAt(LocalDateTime.now());
        if (target == IncidentStatus.CLOSED) incident.setClosedAt(LocalDateTime.now());
        incident.setStatus(target);
        return response(incidents.saveAndFlush(incident));
    }

    private void applyReport(Incident incident, IncidentRequestDto request) {
        Asset asset = request.assetId() == null ? null : assets.findById(request.assetId())
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Asset not found."));
        Room room = asset == null ? rooms.findById(request.roomId())
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Room not found.")) : asset.getRoom();
        if (request.roomId() != null && request.roomId() != room.getId())
            throw new ResponseStatusException(BAD_REQUEST, "The asset does not belong to the selected room.");
        incident.setRoom(room);
        incident.setAsset(asset);
        incident.setCategory(request.category());
        incident.setDescription(request.description().trim());
        incident.setSafetyHazard(request.safetyHazard());
        incident.setOperationalImpact(request.operationalImpact());
        incident.setAffectedArea(request.affectedArea());
        evaluate(incident);
    }

    private void evaluate(Incident incident) {
        // Initial scoring policy: scope (1..3), impact (0/3), and criticality (0/2/4/6).
        // The current schema has one hazard flag, so every safety hazard is Critical.
        int criticality = Math.max(incident.getRoom().getCriticality().ordinal(),
            incident.getAsset() == null ? 0 : incident.getAsset().getCriticality().ordinal());
        int score = Math.min(incident.getAffectedArea(), 3)
            + (incident.getOperationalImpact() ? 3 : 0) + 2 * criticality
            + (incident.getSafetyHazard() ? 13 : 0);
        incident.setPriorityScore(score);
        incident.setPriority(incident.getSafetyHazard() ? IncidentPriority.CRITICAL
            : score >= 7 ? IncidentPriority.HIGH : score >= 4 ? IncidentPriority.MEDIUM : IncidentPriority.LOW);
        incident.setEvaluatedAt(LocalDateTime.now());
        incident.setStatus(IncidentStatus.EVALUATED);
    }

    private User user(String email) {
        if (email == null) throw new ResponseStatusException(UNAUTHORIZED, "Authentication is required.");
        return users.findByEmail(email)
            .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Authenticated user not found."));
    }

    private Incident lockedIncident(long id) {
        return incidents.findForUpdate(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Incident not found."));
    }

    private static void requireAdmin(User user) {
        if (user.getUserRole() != UserRole.ADMIN)
            throw new ResponseStatusException(FORBIDDEN, "Administrator access is required.");
    }

    private static boolean isAssigned(Incident incident, User user) {
        return incident.getAssignedTo() != null && incident.getAssignedTo().getId() == user.getId();
    }

    private static void requireUnworked(Incident incident) {
        // Prevent the database cascade from deleting maintenance history.
        if ((incident.getStatus() != IncidentStatus.REPORTED && incident.getStatus() != IncidentStatus.EVALUATED)
            || !incident.getMaintenanceRecords().isEmpty())
            throw new ResponseStatusException(CONFLICT, "Only unassigned incidents without maintenance history can be edited or deleted.");
    }

    private static IncidentResponseDto response(Incident incident) {
        return new IncidentResponseDto(incident.getId(), incident.getIncidentNumber(), incident.getRoom().getId(),
            incident.getAsset() == null ? null : incident.getAsset().getId(), incident.getReportedBy().getId(),
            incident.getAssignedTo() == null ? null : incident.getAssignedTo().getId(), incident.getCategory(),
            incident.getDescription(), incident.getSafetyHazard(), incident.getOperationalImpact(),
            incident.getAffectedArea(), incident.getPriority(), incident.getPriorityScore(), incident.getStatus(),
            incident.getCreatedAt(), incident.getEvaluatedAt(), incident.getResolvedAt(), incident.getClosedAt());
    }
}
