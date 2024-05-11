package org.example.iotbay.repository;

import org.example.iotbay.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    List<OrderLineItem> findByOrderId(Long orderId);
    List<OrderLineItem> findByProductId(Long productId);
}
