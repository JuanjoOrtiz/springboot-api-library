package com.springboot.api.library.controllers;

import com.springboot.api.library.dtos.LoanRequestDTO;
import com.springboot.api.library.dtos.LoanResponseDTO;
import com.springboot.api.library.services.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get all loans", description = "Retrieve a paginated list of all loans")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of loans retrieve successfully"),
            @ApiResponse(responseCode = "204", description = "Loans not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
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
    @Operation(summary = "Get loan by ID", description = "Retrieve a loan by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "loan found and returned"),
            @ApiResponse(responseCode = "404", description = "loan not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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
    @Operation(summary = "Create a new loan", description = "Create a new loan entry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "loan created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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

    @Operation(summary = "Update a loan", description = "Update an existing loan by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Loan not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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
    @Operation(summary = "Delete a loan", description = "Delete a loan by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "loan deleted successfully"),
            @ApiResponse(responseCode = "404", description = "loan not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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
