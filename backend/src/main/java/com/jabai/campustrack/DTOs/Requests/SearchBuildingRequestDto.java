package com.jabai.campustrack.DTOs.Requests;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class SearchBuildingRequestDto {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate from;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private final LocalDate to;

    private final String name;

    public SearchBuildingRequestDto(LocalDate from, LocalDate to, String name) {
        this.from = from;
        this.to = to;
        this.name = name;
    }

    // Getters
    public LocalDate getFrom() { return from; }
    public LocalDate getTo() { return to; }
    public String getName() { return name; }
}