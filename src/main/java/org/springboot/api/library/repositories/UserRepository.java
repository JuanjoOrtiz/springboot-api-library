package org.springboot.api.library.repositories;

import org.springboot.api.library.entities.Book;
import org.springboot.api.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    @Query("SELECT u FROM User u WHERE b.title LIKE CONCAT('%', :userNumber, '%') ")
    Optional<User> findByUserNumberContaining(String userNumber);
}
