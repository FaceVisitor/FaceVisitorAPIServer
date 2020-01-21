package com.facevisitor.api.service;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.repo.UserRepository;
import com.facevisitor.api.config.security.SecurityUserDetailService;
import com.facevisitor.api.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {
    @Autowired
    UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityUserDetailService userDetailsService;

    @Transactional
    @Test
    public void 유저디테일서비스() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("wndudpower@gmail.com");
        System.out.println(userDetails.getAuthorities());
    }


    @Test
    public void 유저정보() {
        User userByEmail = userService.getUserByEmail("wndudpower@gmail.com");

    }

    @Test
    public void 유저삭제(){
//        userService.deleteUser(45L);
//        userService.deleteUser(46L);
//        userService.deleteUser(47L);
        userService.deleteUser(52L);
    }


}