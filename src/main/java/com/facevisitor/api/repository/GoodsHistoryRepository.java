package com.facevisitor.api.repository;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsHistory;
import com.facevisitor.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface GoodsHistoryRepository extends JpaRepository<GoodsHistory, Long> {
    Optional<GoodsHistory> findFirstByUserAndGoods(User user, Goods goods);

    @Query("select gh from GoodsHistory gh where gh.user = ?1 order by gh.goods.updatedAt desc")
    List<GoodsHistory> findAllByUser(User user);
}
