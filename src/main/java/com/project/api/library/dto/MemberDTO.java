package com.project.api.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;

    private String memberShipNumber;

    private String name;

    private String nif;

    private String brithdayDate;

    private Integer mobile;

    private String address;

    private String email;

    private String province;

    public MemberDTO(String memberShipNumber) {
        this.memberShipNumber = memberShipNumber;
    }
}
