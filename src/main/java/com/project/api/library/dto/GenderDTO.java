package com.project.api.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GenderDTO {
    @NotNull
    @NotBlank(message = "Gender is required")
    private String name;

    public GenderDTO(String name) {
        this.name = name;
    }
}
