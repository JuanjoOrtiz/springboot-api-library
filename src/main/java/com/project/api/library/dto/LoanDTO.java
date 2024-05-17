package com.project.api.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanDTO {


    private Long id;
    private BookDTO book;
    private MemberDTO member;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;

}
