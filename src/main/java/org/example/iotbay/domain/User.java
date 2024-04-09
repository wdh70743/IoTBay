package org.example.iotbay.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotNull(message = "Password cannot be null")
    @Column(length=100)
    private String password;

    @NotNull(message = "FirstName cannot be null")
    @Column(length=50)
    private String firstName;

    @NotNull(message = "LastName cannot be null")
    @Column(length=50)
    private String lastName;

    @NotNull(message = "PhoneNumber cannot be null")
    @Column(length=50)
    private String phoneNumber;

    @NotNull(message = "Role cannot be null")
    @Column(length = 25)
    private String role;

    //    @Column(name = "addressId", nullable = false, unique = true)
//    private Long addressId;
}
