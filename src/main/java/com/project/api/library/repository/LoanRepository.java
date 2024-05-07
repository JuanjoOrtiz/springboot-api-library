package com.project.api.library.repository;

import com.project.api.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LoanRepository extends JpaRepository<Loan, Long> {
}
