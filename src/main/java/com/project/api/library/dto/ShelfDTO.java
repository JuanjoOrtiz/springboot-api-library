package com.project.api.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelfDTO {
    private Long id;
    private String code;

    public ShelfDTO(Long id) {
        this.id = id;
    }

    public ShelfDTO(String code) {
        this.code = code;
    }
}
