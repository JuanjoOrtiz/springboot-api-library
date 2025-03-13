package org.springboot.api.library.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_number", unique = true, updatable = false)
    @Positive
    private int userNumber;


    @NotBlank(message = "firstName cannot be blank")
    @Size(min = 1, max = 30, message = "firstName must be between 1 and 30 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "lastName cannot be blank")
    @Size(min = 2, max = 30, message = "lastName must be between 1 and 30 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotBlank(message = "email cannot be blank")
    @Email
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "The password must contain at least one uppercase letter, one lowercase letter, one number, and one special character."
    )
    private String password;


    @Column(name = "create_at" , updatable = false)
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    private Collection<UserRole> role = new ArrayList<>();


}
