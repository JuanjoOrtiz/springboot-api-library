package com.springboot.api.library.repositories;

import com.springboot.api.library.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
}
