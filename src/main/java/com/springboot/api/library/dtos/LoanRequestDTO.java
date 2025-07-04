package com.springboot.api.library.dtos;

import com.springboot.api.library.entities.Book;
import com.springboot.api.library.entities.LoanStatus;
import com.springboot.api.library.entities.Member;
import com.springboot.api.library.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanRequestDTO(
        Book book,
        Member member,
        User checkedOutBy,
        LocalDateTime checkoutDate,
        LocalDate dueDate,
        LocalDateTime returnDate,
        User receivedBy,
        LoanStatus status,
        Double fineAmount,
        String notes
) {
}
