package com.project.api.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {
    private String bookTitle;
    private String memberShipNumber;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;

    public LoanDTO(BookDTO bookDTO) {
        this. bookTitle = bookDTO.getTitle();
    }
}
