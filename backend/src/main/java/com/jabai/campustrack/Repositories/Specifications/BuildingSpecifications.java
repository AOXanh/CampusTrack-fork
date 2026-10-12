package com.jabai.campustrack.Repositories.Specifications;

import com.jabai.campustrack.Models.Building;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public final class BuildingSpecifications {
    public static Specification<Building> hasCompletedAtRange(LocalDateTime from, LocalDateTime to) {
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

    public static Specification<Building> hasNameKeyword(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null)
                return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }
}