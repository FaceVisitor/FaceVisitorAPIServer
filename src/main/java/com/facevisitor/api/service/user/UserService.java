package com.facevisitor.api.service.user;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    @Transactional(readOnly = true)
    public Boolean exist(String email){
        return userRepository.findByEmail(email).isPresent();
    }


    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
    }
    @Transactional(readOnly = true)
    public User getUserByFaceIds(List<String> faceId){
          return userRepository.findByFaceIds(faceId).orElseThrow(NotFoundException::new);
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }


    public User updateUser(User updated) {
        User ori = userRepository.findById(updated.getId()).orElseThrow(NotFoundException::new);
        modelMapper.map(updated, ori);
        return ori;
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id).orElseThrow(NotFoundException::new);
        user.setAuthorities(null);
        userRepository.deleteById(id);

    }



}
