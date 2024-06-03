package com.project.api.library.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "MemberShipNumber is required")
    private String memberShipNumber;
    @NotNull
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull
    @NotBlank(message = "Nif is required")
    private String nif;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @NotNull
    private Date brithdayDate;
    @NotNull
    @Size(min = 9, max = 9, message = "Mobile length not valid")
    private String mobile;
    @NotNull
    @NotBlank(message = "Address is required")
    private String address;
    @NotNull
    @NotBlank(message = "email is required")
    @Email(message = "Email not valid")
    private String email;
    @NotNull
    @NotBlank(message = "province is required")
    private String province;

}
