package com.facevisitor.api.service.base;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BaseService {

    @Autowired
    UserRepository userRepository;

    public User currentUser(String email){
        return userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
    }

}
