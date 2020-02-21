package com.facevisitor.api.repository;

import com.facevisitor.api.domain.goods.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GoodsRepository extends JpaRepository<Goods,Long> {

  @EntityGraph(attributePaths = {"images","categories"})
  @Query("select g from Goods g where g.name like %?1% ")
  Page<Goods> page(String searchQuery, Pageable pageable);

  @EntityGraph(attributePaths = {"images","categories","store"})
  @Query("select g from Goods g where g.id = ?1")
  Optional<Goods> get(Long id);


}
