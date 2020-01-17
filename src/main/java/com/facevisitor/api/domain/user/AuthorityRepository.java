package com.facevisitor.api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByRole(String roleName);
}
