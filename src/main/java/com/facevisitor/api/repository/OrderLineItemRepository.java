package com.facevisitor.api.repository;

import com.facevisitor.api.domain.order.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem,Long> {
}
