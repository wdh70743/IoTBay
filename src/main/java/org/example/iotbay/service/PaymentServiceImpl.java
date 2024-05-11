package org.example.iotbay.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.iotbay.domain.Order;
import org.example.iotbay.domain.Payment;
import org.example.iotbay.dto.PaymentDTO;
import org.example.iotbay.repository.OrderRepository;
import org.example.iotbay.repository.PaymentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public PaymentDTO.Response createPayment(PaymentDTO.Request request) {
        Payment payment = modelMapper.map(request, Payment.class);

        // Retrieve and set the order associated with the payment
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + request.getOrderId()));
        payment.setOrder(order);  // Make sure the order is set on the payment

        if (payment.getPaymentStatus() == null) {
            payment.setPaymentStatus("Pending");  // Set default payment status if not provided
        }
        payment = paymentRepository.save(payment);
        PaymentDTO.Response response = modelMapper.map(payment, PaymentDTO.Response.class);
        response.setOrderId(order.getId());
        return response;
    }

    @Override
    @Transactional
    public PaymentDTO.Response updatePayment(Long paymentId, PaymentDTO.Request request) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        modelMapper.map(request, payment);
        payment = paymentRepository.save(payment);
        return modelMapper.map(payment, PaymentDTO.Response.class);
    }

    @Override
    @Transactional
    public PaymentDTO.Response getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));
        PaymentDTO.Response response = modelMapper.map(payment, PaymentDTO.Response.class);
        response.setOrderId(payment.getOrder().getId());
        return response;
    }

    @Override
    @Transactional
    public PaymentDTO.Response getPaymentsByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with order ID: " + orderId));
        PaymentDTO.Response response = modelMapper.map(payment, PaymentDTO.Response.class);
        response.setOrderId(payment.getOrder().getId());
        return response;
    }


    @Override
    @Transactional
    public List<PaymentDTO.Response> searchPayments(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);
        return payments.stream()
                .map(payment -> modelMapper.map(payment, PaymentDTO.Response.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));
        paymentRepository.delete(payment);
        return "Payment successfully deleted";
    }
}
