package com.facevisitor.api.service.user;

import com.facevisitor.api.common.exception.NotFoundUserException;
import com.facevisitor.api.domain.security.SecurityUser;
import com.facevisitor.api.domain.user.Authority;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.UserRepository;
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
        log.debug(String.valueOf(user));
        return new SecurityUser(user.getEmail(), user.getPassword(), user.getEnable(), true, true, true, toGranted(user.getAuthorities()));
    }

    public Set<SimpleGrantedAuthority> toGranted(Set<Authority> authorities) {
        return authorities.stream().map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getRole())).collect(Collectors.toSet());
    }


}
