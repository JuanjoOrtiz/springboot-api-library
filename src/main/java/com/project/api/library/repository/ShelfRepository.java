package com.project.api.library.repository;

import com.project.api.library.entity.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    Shelf findByCode(String code);
}
