package com.facevisitor.api;

import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.user.Join;
import com.facevisitor.api.service.auth.AuthService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthTest {

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Transactional
    @Test
    public void 회원가입() {
        Join join = new Join();
        join.setEmail("a@a.com");
        join.setPassword("asdf4112");
//        join.setFaceId("factet");
        join.setPhone("01026588178");
        User join1 = authService.join(join);
        assertThat(join1.getEmail()).isEqualTo("a@a.com");
    }

    @Test
    public void 패스워드인코드() {
        String asdf4112 = passwordEncoder.encode("asdf4112");
        System.out.println(asdf4112);
    }


}
