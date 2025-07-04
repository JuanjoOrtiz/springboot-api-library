package com.springboot.api.library.services;

import com.springboot.api.library.dtos.LoanRequestDTO;
import com.springboot.api.library.dtos.LoanResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoanService {
    Page<LoanResponseDTO> findAll(Pageable pageable);
    Optional<LoanResponseDTO> findById(Long id);
    LoanResponseDTO create(LoanRequestDTO loanRequestDTO);
    LoanResponseDTO update(LoanRequestDTO loanRequestDTO, Long id);
    boolean delete(Long id);
}
