package com.project.api.library.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class LoanDTO {

    @NotNull
    private String book;
    @NotNull
    private String memberShipNumber;
    @NotNull
    private LocalDateTime loanDate;
    @NotNull
    private LocalDateTime returnDate;

}
