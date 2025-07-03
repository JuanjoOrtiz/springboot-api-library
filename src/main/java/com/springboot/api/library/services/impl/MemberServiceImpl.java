package com.springboot.api.library.services.impl;

import com.springboot.api.library.dtos.MemberRequestDTO;
import com.springboot.api.library.dtos.MemberResponseDTO;
import com.springboot.api.library.services.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class MemberServiceImpl implements MemberService {
    @Override
    public Page<MemberResponseDTO> findAll(Pageable pageable) {

    }

    @Override
    public Optional<MemberResponseDTO> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public MemberResponseDTO create(MemberRequestDTO memberRequestDTO) {
        return null;
    }

    @Override
    public MemberResponseDTO update(MemberRequestDTO memberRequestDTO, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
