package com.facevisitor.api.repository;

import com.facevisitor.api.domain.face.FaceMeta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaceMetaRepository extends JpaRepository<FaceMeta, Long> {
}
