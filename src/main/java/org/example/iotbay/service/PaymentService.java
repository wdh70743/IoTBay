package org.example.iotbay.service;

import org.example.iotbay.dto.PaymentDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentService {
    PaymentDTO.Response createPayment(PaymentDTO.Request request);

    PaymentDTO.Response updatePayment(Long paymentId, PaymentDTO.Request request);

    PaymentDTO.Response getPaymentById(Long paymentId);

    PaymentDTO.Response getPaymentsByOrderId(Long orderId);

    List<PaymentDTO.Response> searchPayments(LocalDateTime startDate, LocalDateTime endDate);

    String deletePayment(Long paymentId);

}
