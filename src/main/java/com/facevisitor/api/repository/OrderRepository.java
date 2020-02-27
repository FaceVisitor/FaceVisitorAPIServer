package com.facevisitor.api.repository;

import com.facevisitor.api.domain.order.FVOrder;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<FVOrder,Long> {

    @EntityGraph(attributePaths = {"lineItems.goods","point","user"})
    @Query("select o from FVOrder o where o.id = ?1")
    Optional<FVOrder> get(Long id);
}
