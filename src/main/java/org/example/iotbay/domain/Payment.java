package org.example.iotbay.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @NotNull(message = "Amount cannot be null")
    @Column(nullable = false)
    private Double amount;

    @NotNull(message = "Payment method cannot be null")
    @Column(nullable = false, length = 50)
    private String paymentMethod;

    @NotNull(message = "Card last four digits cannot be null")
    @Column(nullable = false, length=4)
    private String cardLastFourDigits;

    @NotNull(message = "Payment status cannot be null")
    @Column(nullable = false, length = 20)
    private String paymentStatus;

    @NotNull(message = "Payment date cannot be null")
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime paymentDate;
}
