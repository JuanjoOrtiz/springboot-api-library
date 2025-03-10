package org.springboot.api.library.dtos;

import java.time.LocalDate;

public record UserDTO(

        String userNumber,
        String firstName,
        String lastName,
        String dni,
        LocalDate createAt,
        String email,
        int password
     ) {

}
