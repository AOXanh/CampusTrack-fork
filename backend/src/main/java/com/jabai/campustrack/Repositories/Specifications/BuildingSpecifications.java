package com.jabai.campustrack.Repositories.Specifications;

import com.jabai.campustrack.Models.Building;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Locale;

public final class BuildingSpecifications {
    public static Specification<Building> hasCreatedAtRange(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) -> {
            if (from == null && to == null)
                return null;
            if (from == null)
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), to);
            if (to == null)
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), from);
            return criteriaBuilder.between(root.get("createdAt"), from, to);
        };
    }

    public static Specification<Building> hasNameKeyword(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null)
                return null;
            String keyword = name
                    .trim()
                    .toLowerCase(Locale.ROOT)
                    .replace("\\", "\\\\")
                    .replace("%", "\\%")
                    .replace("_", "\\_");
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + keyword + "%", '\\');
        };
    }
}