package com.project.api.library.repository.auth;

import com.project.api.library.entity.auth.Role;
import com.project.api.library.entity.auth.RoleEnum;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Optional<Role> findByName(RoleEnum name);
}
