package org.example.iotbay.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(length = 100)
    @NotNull(message = "Name cannot be null")
    private String name;

    @Column(length = 10000)
    @NotNull(message = "Description cannot be null")
    private String description;

    @Column(nullable = false, length=100)
    @NotNull(message = "Type cannot be null")
    private String type;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message="Quantity cannot be negative number")
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message="Price cannot be negative number")
    private Integer price;


}
