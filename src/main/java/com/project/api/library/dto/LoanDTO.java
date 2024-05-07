package com.project.api.library.dto;

import lombok.Data;

@Data
public class LoanDTO {


    private Long id;
    private String book;
    private String member;
    private String loanDate;
    private String returnDate;

}
