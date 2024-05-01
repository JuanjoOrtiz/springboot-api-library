package com.project.api.library.service;

import com.project.api.library.dto.MemberDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberService {

    Page<MemberDTO> findAll(Pageable pageable);
    Optional<MemberDTO> findById(Long id);
    MemberDTO save(MemberDTO memberDTO);
    MemberDTO update(Long id,MemberDTO memberDTO);
    void delete(Long id);
}
