package com.jabai.campustrack.DTOs.Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PdfParserRequestDto(
        ReportInfo report,
        @JsonProperty("involved_parties") List<InvolvedParty> involvedParties,
        IncidentDetails details,
        Preparer preparer
) {

    public record ReportInfo(
            @NotBlank @JsonProperty("report_id") String reportId,
            @NotBlank  @JsonProperty("date_filed") String dateFiled,
            @NotBlank @JsonProperty("incident_date") String incidentDate,
            @NotBlank @JsonProperty("incident_time") String incidentTime,
            @NotBlank String location,
            @NotBlank Severity severity
    ) {}

    public record InvolvedParty(
            @NotBlank @JsonProperty("id_number") String idNumber,
            @NotBlank String name,
            @NotBlank String role,
            @NotBlank String department,
            @NotBlank String contact
    ) {}

    public record IncidentDetails(
            @NotBlank String description,
            @NotBlank @JsonProperty("action_taken") String actionTaken
    ) {}

    public record Preparer(
            @NotBlank String name,
            @NotBlank String title,
            @NotBlank String department
    ) {}

    public enum Severity {
        HIGH("High"),
        MEDIUM("Medium"),
        LOW("Low");

        private final String value;

        Severity(String value) { this.value = value; }

        @JsonValue
        public String value() { return value; }
    }
}