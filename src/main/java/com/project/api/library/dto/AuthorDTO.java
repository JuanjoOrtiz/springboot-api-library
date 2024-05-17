package com.project.api.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDTO {
    private Long id;
    private String name;

    public AuthorDTO(Long id) {
        this.id = id;
    }

    public AuthorDTO(String name) {
        this.name = name;
    }
}
