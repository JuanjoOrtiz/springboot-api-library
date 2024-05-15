package com.project.api.library.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PublisherDTO {
    private Long id;
    private String name;

    public PublisherDTO(Long id) {
        this.id = id;
    }
}
