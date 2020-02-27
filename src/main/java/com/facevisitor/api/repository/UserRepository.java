package com.facevisitor.api.repository;

import com.facevisitor.api.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
    @EntityGraph(attributePaths = {"authorities","faceMeta.faceId", "faceMeta.faceImages","points"})
    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("select u from User u join fetch u.authorities join fetch u.faceMeta fm join fetch fm.faceId fi where fi.faceId in (?1)")
    Optional<User> findByFaceIds(List<String> faceIds);

}
