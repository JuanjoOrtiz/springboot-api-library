package com.project.api.library.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    @NotNull
    @NotBlank(message = "MemberShipNumber is required")
    @Column(unique = true)
    private String memberShipNumber;
    @NotNull
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull
    @NotBlank(message = "Nif is required")
    private String nif;
    @NotNull
    private String brithdayDate;
    @NotNull
    @Size(min = 9, max = 9, message = "Mobile length not valid")
    private String mobile;
    @NotNull
    @NotBlank(message = "Address is required")
    private String address;
    @NotNull
    @NotBlank(message = "Email is required")
    @Email(message = "Email not valid")
    private String email;
    @NotNull
    @NotBlank(message = "Province is required")
    private String province;

    public MemberDTO(String memberShipNumber) {
        this.memberShipNumber = memberShipNumber;
    }
}
