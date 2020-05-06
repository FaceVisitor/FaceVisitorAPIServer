package com.facevisitor.api.repository;

import com.facevisitor.api.domain.order.FVOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<FVOrder, Long> {

    @EntityGraph(attributePaths = {"lineItems.goods", "point", "user"})
    @Query("select o from FVOrder o where o.user.id = ?1")
    Page<FVOrder> pageable(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"lineItems.goods", "point", "user"})
    @Query("select o from FVOrder o where o.id = ?1")
    Optional<FVOrder> get(Long id);
}
