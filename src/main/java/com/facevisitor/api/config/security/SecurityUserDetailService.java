package com.facevisitor.api.config.security;

import com.facevisitor.api.common.exception.NotFoundUserException;
import com.facevisitor.api.domain.security.Authority;
import com.facevisitor.api.domain.security.SecurityUser;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class SecurityUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NotFoundUserException(email + "을 찾을 수 없습니다"));
        return new SecurityUser(user.getId(),user.getEmail(), user.getName(),user.getPassword(),toGranted(user.getAuthorities()));
    }

    public Set<SimpleGrantedAuthority> toGranted(Set<Authority> authorities) {
        return authorities.stream().map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getRole())).collect(Collectors.toSet());
    }

    @Transactional
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return SecurityUser.create(user);
    }


}
