package com.project.api.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AuthorDTO {

    @NotNull
    @NotBlank(message = "Author is required")
    private String name;


    public AuthorDTO(String name) {
        this.name = name;
    }
}
