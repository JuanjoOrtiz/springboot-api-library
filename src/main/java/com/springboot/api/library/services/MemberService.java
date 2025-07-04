package com.springboot.api.library.services;

import com.springboot.api.library.dtos.MemberRequestDTO;
import com.springboot.api.library.dtos.MemberResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface MemberService {
    Page<MemberResponseDTO> findAll(Pageable pageable);
    Optional<MemberResponseDTO> findById(Long id);
    MemberResponseDTO create(MemberRequestDTO memberRequestDTO);
    MemberResponseDTO update(MemberRequestDTO memberRequestDTO, Long id);
    boolean delete(Long id);
}
