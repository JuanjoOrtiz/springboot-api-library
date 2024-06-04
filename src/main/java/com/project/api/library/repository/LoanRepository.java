package com.project.api.library.repository;

import com.project.api.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LoanRepository extends JpaRepository<Loan, Long> {
    Optional<Loan> findByBookTitle(String title);
}
