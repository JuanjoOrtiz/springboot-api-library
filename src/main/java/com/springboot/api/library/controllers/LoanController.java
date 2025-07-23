package com.springboot.api.library.controllers;

import com.springboot.api.library.dtos.LoanRequestDTO;
import com.springboot.api.library.dtos.LoanResponseDTO;
import com.springboot.api.library.services.LoanService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;


    @GetMapping
    public ResponseEntity<Page<?>> findAllLoans(@PageableDefault(size = 20, sort = "id") Pageable pageable) {
        try {
            log.info("Fetching all loans with pagination: page={}, size={}",
                    pageable.getPageNumber(), pageable.getPageSize());

            Page<LoanResponseDTO> loans = loanService.findAll(pageable);
            if (loans.isEmpty()) {
                log.info("Loans not found");
                return ResponseEntity.noContent().build();
            }
            log.info("Loans found: {}", loans.getTotalElements());
            return ResponseEntity.ok(loans);
        } catch (Exception e) {
            log.error("Error occurred while fetching loans", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findloanById(@PathVariable Long id) {
        try {
            log.info("Fetching loan by Id: {}", id);

            return loanService.findById(id)
                    .map(loan -> {
                        log.info("loan found with id: {}", id);
                        return ResponseEntity.ok(loan);
                    })
                    .orElseGet(() -> {
                        log.info("loan not found with id: {}", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Error occurred while fetching loan with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createloan(@Valid @RequestBody LoanRequestDTO loanRequest) {
        try {
            log.info("Creating new loan: {}", loanRequest);
            LoanResponseDTO createdLoan = loanService.create(loanRequest);
            log.info("loan created successfully with id: {}", createdLoan.id());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLoan);
        } catch (Exception e) {
            log.error("Error occurred while creating loan", e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> updateloan(
            @PathVariable Long id,
            @Valid @RequestBody LoanRequestDTO loanRequestDto) {
        try {
            log.info("Updating loan with id: {}", id);
            LoanResponseDTO updatedloan = loanService.update(loanRequestDto, id);
            log.info("loan updated successfully with id: {}", id);
            return ResponseEntity.ok(updatedloan);
        } catch (EntityNotFoundException e) {
            log.warn("loan not found for update with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error occurred while updating loan with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteloan(@PathVariable Long id) {
        try {
            log.info("Deleting loan with id: {}", id);
            if (loanService.delete(id)) {
                log.info("loan deleted successfully with id: {}", id);
                return ResponseEntity.noContent().build();
            } else {
                log.info("loan not found for deletion with id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting loan with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    
}
