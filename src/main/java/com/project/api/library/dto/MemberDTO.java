package com.project.api.library.dto;

import lombok.Data;

@Data
public class MemberDTO {
    private Long id;

    private String membershipnumber;

    private String name;

    private String nif;

    private String brithdayDate;

    private Integer mobile;

    private String address;

    private String email;

    private String province;

}
