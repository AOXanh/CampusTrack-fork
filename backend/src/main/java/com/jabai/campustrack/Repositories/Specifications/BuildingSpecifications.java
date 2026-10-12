package com.jabai.campustrack.Repositories.Specifications;

import com.jabai.campustrack.Models.Building;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class BuildingSpecifications {

    private BuildingSpecifications() {
    }

    public static Specification<Building> createdAtFrom(LocalDate from) {
        LocalDateTime startDate = from.atStartOfDay();

        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<LocalDateTime>get("createdAt"), startDate);
    }

    public static Specification<Building> createdAtTo(LocalDate to) {LocalDateTime endDate = to.plusDays(1).atStartOfDay();

        return (root, query, cb) -> cb.lessThan(root.<LocalDateTime>get("createdAt"), endDate);
    }

    public static Specification<Building> nameContains(String name) {String keyword = "%" + name.trim().toLowerCase() + "%";

        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), keyword);
    }
}