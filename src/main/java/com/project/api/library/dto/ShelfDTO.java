package com.project.api.library.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ShelfDTO {
    @NotNull
    @NotBlank(message = "Shelf is required")
    private String code;

    public ShelfDTO(String code) {
        this.code = code;
    }
}
