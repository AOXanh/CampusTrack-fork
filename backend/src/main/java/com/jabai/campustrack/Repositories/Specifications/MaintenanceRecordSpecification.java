package com.jabai.campustrack.Repositories.Specifications;

import com.jabai.campustrack.Models.MaintenanceRecord;
import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class MaintenanceRecordSpecification {
    public static Specification<MaintenanceRecord> hasIncidentId(Long incidentId) {
        return (root, query, criteriaBuilder) -> {
            if (incidentId == null)
                return null;
            return criteriaBuilder.equal(root.get("incident").get("id"), incidentId);
        };
    }

    public static Specification<MaintenanceRecord> hasUserId(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null)
                return null;
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<MaintenanceRecord> hasActionKeyword(String action) {
        return (root, query, criteriaBuilder) -> {
            if (action == null || action.isBlank())
                return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("action")), "%" + action.toLowerCase() + "%");
        };
    }

    public static Specification<MaintenanceRecord> hasRemarksKeyword(String remarks) {
        return (root, query, criteriaBuilder) -> {
            if (remarks == null || remarks.isBlank())
                return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("remarks")), "%" + remarks.toLowerCase() + "%");
        };
    }

    public static Specification<MaintenanceRecord> hasStatus(MaintenanceRecordStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null)
                return null;
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<MaintenanceRecord> hasCompletedAtRange(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (from == null && to == null)
                return null;
            if (from == null)
                return criteriaBuilder.lessThanOrEqualTo(root.get("completedAt"), to);
            if (to == null)
                return criteriaBuilder.greaterThanOrEqualTo(root.get("completedAt"), from);
            return criteriaBuilder.between(root.get("completedAt"), from, to);
        };
    }
}