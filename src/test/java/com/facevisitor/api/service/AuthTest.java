package com.facevisitor.api.service;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.user.Join;
import com.facevisitor.api.repository.UserRepository;
import com.facevisitor.api.service.auth.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;



//    @Transactional
    @Test
    public void 회원가입() {
        Join join = new Join();
        join.setEmail("a@a.com");
        join.setPassword("asdf4112");
        join.setFaceIds(Arrays.asList("a","b"));
        join.setPhone("01026588178");
        join.setGender("Male");
        join.setLowAge(12);
        join.setHighAge(24);
        User join1 = authService.join(join);
        assertThat(join1.getEmail()).isEqualTo("a@a.com");
    }

    @Test
    public void 패스워드인코드() {
        String asdf4112 = passwordEncoder.encode("asdf4112");
        System.out.println(asdf4112);
    }

    @Test
    public void FIND_BY_FACEIDS(){
        List<String> faceIDs = new ArrayList();
        faceIDs.add("a");
        faceIDs.add("b");
        User user = userRepository.findByFaceIds(faceIDs).orElse(null);
        System.out.println(user);
    }

    @Test
    public void 로그인(){
        Map<String, String> tokenData = authService.login(Arrays.asList("98373c74-b0bd-40bc-9b39-8174999dd6cf"));
        assertThat(tokenData.get("access_token")).isNotEmpty();
        assertThat(tokenData.get("refresh_token")).isNotEmpty();

    }


}
