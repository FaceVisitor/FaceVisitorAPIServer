package com.facevisitor.api.repository;

import com.facevisitor.api.domain.store.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store,Long> {
    List<Store> findByUserEmail(String email);


}
