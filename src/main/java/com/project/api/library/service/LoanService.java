package com.project.api.library.service;

import com.project.api.library.dto.LoanDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LoanService{
    Page<LoanDTO> findAll(Pageable pageable);
   Optional<LoanDTO> findById(Long id);
    LoanDTO save(LoanDTO loanDTO);
    LoanDTO update(Long id,LoanDTO loanDTO);
    void delete(Long id);
}