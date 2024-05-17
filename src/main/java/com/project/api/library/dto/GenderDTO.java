package com.project.api.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenderDTO {
    private Long id;
    private String name;

    public GenderDTO(Long id) {
        this.id = id;
    }

    public GenderDTO(String name) {
        this.name = name;
    }
}
