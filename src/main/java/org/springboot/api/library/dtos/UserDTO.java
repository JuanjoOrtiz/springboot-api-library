package org.springboot.api.library.dtos;

import jakarta.validation.constraints.Pattern;

public record UserDTO(


        String firstName,
        String lastName,
        String email,

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "The password must contain at least one uppercase letter, one lowercase letter, one number, and one special character."
        )
        String password
     ) {

}
