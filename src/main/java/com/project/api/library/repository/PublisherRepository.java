package com.project.api.library.repository;

import com.project.api.library.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository  extends JpaRepository<Publisher, Long> {
    Publisher findByName(String name);
}
