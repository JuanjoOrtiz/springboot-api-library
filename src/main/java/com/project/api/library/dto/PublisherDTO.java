package com.project.api.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PublisherDTO {

    @NotNull
    @NotBlank(message = "Member is required")
    private String name;


    public PublisherDTO(String name) {
        this.name = name;
    }
}
