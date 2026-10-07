package com.jabai.campustrack.Repositories.Specifications;

import com.jabai.campustrack.Models.MaintenanceRecord;
import com.jabai.campustrack.Models.Enums.MaintenanceRecordStatus;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MaintenanceRecordSpecification {

    public static Specification<MaintenanceRecord> buildSearchSpec(
            Long incidentId, Long userId, String action, String remarks,
            MaintenanceRecordStatus status, LocalDate startDate, LocalDate endDate) {
        
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (incidentId != null) {
                predicates.add(criteriaBuilder.equal(root.get("incident").get("id"), incidentId));
            }
            if (userId != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), userId));
            }
            if (action != null && !action.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("action")), "%" + action.toLowerCase() + "%"));
            }
            if (remarks != null && !remarks.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("remarks")), "%" + remarks.toLowerCase() + "%"));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("completedAt"), startDate.atStartOfDay()));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("completedAt"), endDate.atTime(23, 59, 59)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}