package com.project.api.library.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GenderDTO {
    private Long id;
    private String name;

    public GenderDTO(Long id) {
        this.id = id;
    }
}
