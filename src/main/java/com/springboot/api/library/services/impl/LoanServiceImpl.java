package com.springboot.api.library.services.impl;

import com.springboot.api.library.dtos.LoanRequestDTO;
import com.springboot.api.library.dtos.LoanResponseDTO;
import com.springboot.api.library.exceptions.ResourceNotFoundException;
import com.springboot.api.library.mappers.LoanMapper;
import com.springboot.api.library.repositories.LoanRepository;
import com.springboot.api.library.services.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final LoanMapper loanMapper;


    @Override
    public Page<LoanResponseDTO> findAll(Pageable pageable) {
        log.debug("Fetching all loans with pagination: {}", pageable);
        return loanRepository.findAll(pageable).map(loanMapper::toDTO);
    }

    @Override
    public Optional<LoanResponseDTO> findById(Long id) {
        log.debug("Searching loan by Id: {}", id);
        return loanRepository.findById(id).map(loanMapper::toDTO);
    }

    @Override
    public LoanResponseDTO create(LoanRequestDTO loanRequestDTO) {
        log.debug("Creating new loan with data: {}", loanRequestDTO);

        var loan = loanMapper.toEntity(loanRequestDTO);
        loan = loanRepository.save(loan);

        log.info("Created new loan with id: {}", loan.getId());
        return loanMapper.toDTO(loan);
    }

    @Override
    public LoanResponseDTO update(LoanRequestDTO loanRequestDTO, Long id) {

        log.debug("Updating loan with id {} with data: {}", id, loanRequestDTO);

        var existingLoan = loanRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("loan not found with id: {}", id);
                    return new ResourceNotFoundException("Loan not found with id: " + id);
                });

        loanMapper.updateEntityFromDto(loanRequestDTO, existingLoan);
        var updatedLoan = loanRepository.save(existingLoan);

        log.info("Updated loan with id: {}", id);
        return loanMapper.toDTO(updatedLoan);


    }

    @Override
    public boolean delete(Long id) {
        log.debug("Deleting loan with id: {}", id);

        if (!loanRepository.existsById(id)) {
            log.error("Attempt to delete non-existent loan with id: {}", id);
            throw new RuntimeException("Loan not found with id: " + id);
        }

        loanRepository.deleteById(id);
        log.info("Deleted loan with id: {}", id);

        return false;
    }
}

