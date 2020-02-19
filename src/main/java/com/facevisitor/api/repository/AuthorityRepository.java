package com.facevisitor.api.repository;

import com.facevisitor.api.domain.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByRole(String roleName);
}
