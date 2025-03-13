package org.springboot.api.library.repositories;

import org.springboot.api.library.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT COALESCE(MAX(u.userNumber), 0) FROM User u")
    int findMaxUserNumber();

    @Query("SELECT u FROM User u WHERE CAST(u.userNumber AS string) LIKE %:userNumber%")
    Optional<User> findByUserNumberContaining(int userNumber);
}
