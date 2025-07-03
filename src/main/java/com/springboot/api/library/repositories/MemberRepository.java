package com.springboot.api.library.repositories;

import com.springboot.api.library.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
}
