    package com.jabai.campustrack.DTOs.Requests;

    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Size;

    public class CreateBuildingRequestDto {
        @NotBlank(message = "Building name is required.")
        @Size(max = 100, message = "Building name must be 0 to 100 characters only.")
        private final String name;

        public CreateBuildingRequestDto(String name) {
            this.name = name;
        }

        public String getName() { return name; }
    }