package org.example.iotbay.repository;

import org.example.iotbay.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    List<Payment> findByPaymentDateBetween(LocalDateTime start, LocalDateTime end);

    List<Payment> findByPaymentStatus(String status);
}
