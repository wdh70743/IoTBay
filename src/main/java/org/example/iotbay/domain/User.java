package org.example.iotbay.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

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
    @Column(length=50, unique=true)
    private String phoneNumber;

    @NotNull(message = "Address cannot be null")
    @Column(length=500)
    private String address;

    @NotNull(message = "Role cannot be null")
    @Column(length = 25)
    private String role;

    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private final Set<UserLog> userLogs = new LinkedHashSet<>();

    //    @Column(name = "addressId", nullable = false, unique = true)
//    private Long addressId;
}
