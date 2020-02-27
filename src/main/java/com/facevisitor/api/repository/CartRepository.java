package com.facevisitor.api.repository;

import com.facevisitor.api.domain.cart.Cart;
import com.facevisitor.api.domain.goods.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,Long> {
    List<Cart> findByUserEmail(String email);
    Optional<Cart> findByGoods(Goods goods);
}
