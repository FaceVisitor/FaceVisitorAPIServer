package com.facevisitor.api.repository;

import com.facevisitor.api.domain.goods.GoodsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsCategoryRepository extends JpaRepository<GoodsCategory,Long> {
}
