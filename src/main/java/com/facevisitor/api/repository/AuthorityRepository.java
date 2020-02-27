package com.facevisitor.api.repository;

import com.facevisitor.api.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByRole(String roleName);
}
