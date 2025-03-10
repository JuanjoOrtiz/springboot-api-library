package org.springboot.api.library.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "firstName cannot be blank")

    @Column(name = "user_number", unique = true)
    @NotBlank(message = "userNumber cannot be blank")
    @Size(min = 5, max = 20, message = "userNumber must be between 5 and 20 characters")
    private String userNumber;
    @Size(min = 1, max = 30, message = "firstName must be between 1 and 30 characters")
    private String firstName;
    @NotBlank(message = "lastName cannot be blank")
    @Size(min = 2, max = 30, message = "lastName must be between 1 and 30 characters")
    private String lastName;
    @NotBlank(message = "dni cannot be blank")
    @Size(min = 9, max = 9, message = "dni must be be 9 characters")
    @Column(name = "dni", unique = true)
    private String dni;
    @Column(name = "create_at")
    @NotNull(message = "createAt cannot be null")
    @PastOrPresent(message = "createAt cannot be in the future")
    private LocalDate createAt;
    @NotBlank(message = "email cannot be blank")
    private String email;
    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 64, message = "La contraseña debe tener entre 8 y 64 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "The password must contain at least one uppercase letter, one lowercase letter, one number and one special character.")
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = Role.USER;
        }
    }
}
