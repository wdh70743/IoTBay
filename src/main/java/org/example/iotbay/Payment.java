package org.example.iotbay;

import jakarta.persistence.*;
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
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @Column(name = "cardNumber")
    private String cardNumber;
    @Column(name = "cvc")
    private String cvc;
    @Column(name = "cardPassword")
    private String cardPassword;
    @CreatedDate
    private Date createdAT;
}
