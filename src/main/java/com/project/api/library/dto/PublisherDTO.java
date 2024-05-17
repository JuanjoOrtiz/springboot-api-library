package com.project.api.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublisherDTO {
    private Long id;
    private String name;

    public PublisherDTO(Long id) {

        this.id = id;
    }

    public PublisherDTO(String name) {
        this.name = name;
    }
}
