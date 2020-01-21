package com.facevisitor.api.config;

import com.facevisitor.api.domain.security.AuthorityRepository;
import com.facevisitor.api.config.security.SecurityUserDetailService;
import com.facevisitor.api.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    SecurityUserDetailService userDetailService;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private UserService authService;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
//        User user = new User();
//        user.setEmail("wndudpower@gmail.com");
//        user.setPassword("asdf4112");
//        user.setEnable(true);
//        Set<Authority> authorities = new HashSet<>();
//        authorities.add(Authority.builder().role_name("USER").build());
//        authorities.add(Authority.builder().role_name("ADMIN").build());
//        authorities.add(Authority.builder().role_name("SUPER").build());
//        List<Authority> authorities1 = authorityRepository.saveAll(authorities);
//        user.setAuthorities(authorities1.stream().collect(Collectors.toSet()));
//        userService.createUser(user);
//
//        UserDetails userDetails = userDetailService.loadUserByUsername("wndudpower@gmail.com");
//        System.out.println(userDetails.getUsername());
    }


//    @Override
//    public void run(String... args) throws Exception {
//
//    }
}
