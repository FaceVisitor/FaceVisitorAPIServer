package com.facevisitor.api.service.user;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }


    public Boolean exist(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
    }

    public User getUserByFaceIds(List<String> faceId){
          return userRepository.findByFaceIds(faceId);
    }


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
