package com.springboot.api.library.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {

    private Long id;
    private int memberShipNumber;
    private String username;
    private String password;
    private LocalDate created_at;


}
