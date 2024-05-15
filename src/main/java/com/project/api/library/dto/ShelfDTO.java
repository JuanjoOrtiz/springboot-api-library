package com.project.api.library.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ShelfDTO {
    private Long id;
    private String code;

    public ShelfDTO(Long id) {
        this.id = id;
    }
}
