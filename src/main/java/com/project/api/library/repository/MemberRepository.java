package com.project.api.library.repository;

import com.project.api.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
Optional<Member> findByMemberShipNumber(String memberShipNumber);
}
