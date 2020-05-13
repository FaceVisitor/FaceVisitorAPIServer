package com.facevisitor.api.owner.service;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.repository.UserToStoreRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@AllArgsConstructor
public class ManageService {

    UserToStoreRepository userToStoreRepository;

    public List<User> findUsersByStoreId(Long storeId){
        return userToStoreRepository.findUsersByStoreId(storeId);
    }


}
