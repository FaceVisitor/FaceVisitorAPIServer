package com.facevisitor.api.repository;

import com.facevisitor.api.domain.store.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreImageRepository extends JpaRepository<StoreImage,Long> {
}
