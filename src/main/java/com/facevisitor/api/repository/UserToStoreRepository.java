package com.facevisitor.api.repository;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.UserToStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserToStoreRepository extends JpaRepository<UserToStore,Long> {

    @Query("select distinct uts.user from UserToStore uts join uts.user where uts.store.id = ?1")
    List<User> findUsersByStoreId(Long id);

}
