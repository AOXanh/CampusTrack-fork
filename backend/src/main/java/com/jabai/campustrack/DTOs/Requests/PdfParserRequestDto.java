package com.jabai.campustrack.DTOs.Requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public class PdfParserRequestDto {
    private final ReportInfo report;

    @JsonProperty("involved_parties")
    private final List<InvolvedParty> involvedParties;

    private final IncidentDetails details;
    private final Preparer preparer;

    @JsonCreator
    public PdfParserRequestDto(
            @JsonProperty("report") ReportInfo report,
            @JsonProperty("involved_parties") List<InvolvedParty> involvedParties,
            @JsonProperty("details") IncidentDetails details,
            @JsonProperty("preparer") Preparer preparer
    ) {
        this.report = report;
        this.involvedParties = involvedParties;
        this.details = details;
        this.preparer = preparer;
    }

    public ReportInfo getReport() { return report; }
    public List<InvolvedParty> getInvolvedParties() { return involvedParties; }
    public IncidentDetails getDetails() { return details; }
    public Preparer getPreparer() { return preparer; }

    public static class ReportInfo {
        @NotBlank
        @JsonProperty("report_id")
        private final String reportId;

        @NotBlank
        @JsonProperty("date_filed")
        private final String dateFiled;

        @NotBlank
        @JsonProperty("incident_date")
        private final String incidentDate;

        @NotBlank
        @JsonProperty("incident_time")
        private final String incidentTime;

        @NotBlank
        private final String location;

        @NotBlank
        private final Severity severity;

        @JsonCreator
        public ReportInfo(
                @JsonProperty("report_id") String reportId,
                @JsonProperty("date_filed") String dateFiled,
                @JsonProperty("incident_date") String incidentDate,
                @JsonProperty("incident_time") String incidentTime,
                @JsonProperty("location") String location,
                @JsonProperty("severity") Severity severity
        ) {
            this.reportId = reportId;
            this.dateFiled = dateFiled;
            this.incidentDate = incidentDate;
            this.incidentTime = incidentTime;
            this.location = location;
            this.severity = severity;
        }

        public String getReportId() { return reportId; }
        public String getDateFiled() { return dateFiled; }
        public String getIncidentDate() { return incidentDate; }
        public String getIncidentTime() { return incidentTime; }
        public String getLocation() { return location; }
        public Severity getSeverity() { return severity; }
    }

    public static class InvolvedParty {
        @NotBlank
        @JsonProperty("id_number")
        private final String idNumber;

        @NotBlank
        private final String name;

        @NotBlank
        private final String role;

        @NotBlank
        private final String department;

        @NotBlank
        private final String contact;

        @JsonCreator
        public InvolvedParty(
                @JsonProperty("id_number") String idNumber,
                @JsonProperty("name") String name,
                @JsonProperty("role") String role,
                @JsonProperty("department") String department,
                @JsonProperty("contact") String contact
        ) {
            this.idNumber = idNumber;
            this.name = name;
            this.role = role;
            this.department = department;
            this.contact = contact;
        }

        public String getIdNumber() { return idNumber; }
        public String getName() { return name; }
        public String getRole() { return role; }
        public String getDepartment() { return department; }
        public String getContact() { return contact; }
    }

    public static class IncidentDetails {
        @NotBlank
        private final String description;

        @NotBlank
        @JsonProperty("action_taken")
        private final String actionTaken;

        @JsonCreator
        public IncidentDetails(
                @JsonProperty("description") String description,
                @JsonProperty("action_taken") String actionTaken
        ) {
            this.description = description;
            this.actionTaken = actionTaken;
        }

        public String getDescription() { return description; }
        public String getActionTaken() { return actionTaken; }
    }

    public static class Preparer {
        @NotBlank
        private final String name;

        @NotBlank
        private final String title;

        @NotBlank
        private final String department;

        @JsonCreator
        public Preparer(
                @JsonProperty("name") String name,
                @JsonProperty("title") String title,
                @JsonProperty("department") String department
        ) {
            this.name = name;
            this.title = title;
            this.department = department;
        }

        public String getName() { return name; }
        public String getTitle() { return title; }
        public String getDepartment() { return department; }
    }

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
