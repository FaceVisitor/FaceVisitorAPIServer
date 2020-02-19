package com.facevisitor.api.controller;

import com.facevisitor.api.dto.auth.TokenDto;
import com.facevisitor.api.owner.dto.auth.OJoin;
import com.facevisitor.api.owner.dto.auth.OLogin;
import com.facevisitor.api.service.auth.AuthService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OwnerTest {

    @Value("${secret.oauth.clientId}")
    String clientId;

    @Value("${secret.oauth.clientSecurity}")
    String clientSecurity;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthService authService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Transactional
    public void 회원가입() throws Exception {
        OJoin join = new OJoin();
        join.setEmail("owner5.com");
        join.setPassword("asdf4112");
        join.setName("허주영 사장");
        join.setPhone("01026588178");
        mockMvc.perform(
                post("/api/v1/owner/auth/join").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(join)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }


    @Test
    public void 로그인() throws Exception {
        OLogin oLogin = new OLogin();
        oLogin.setEmail("sajang@facevisitor.com");
        oLogin.setPassword("asdf4112");
        mockMvc.perform(
                post("/api/v1/owner/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(oLogin)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void 리프레시토큰() throws Exception{
        OLogin oLogin = new OLogin();
        oLogin.setEmail("sajang@facevisitor.com");
        oLogin.setPassword("asdf4112");
        String contentAsString = mockMvc.perform(
                post("/api/v1/owner/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(oLogin)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        ModelMapper modelMapper = new ModelMapper();
        TokenDto tokenDto = modelMapper.map(contentAsString, TokenDto.class);
        String refresh_token = tokenDto.getRefresh_token();
        HashMap<String,String> refreshToken = new HashMap<>();
        refreshToken.put("refresh_token",refresh_token);
        mockMvc.perform(post("/api/v1/onwer/auth/refresh_token").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(refreshToken))
        ).andExpect(status().is2xxSuccessful()).andDo(print());

    }

    @Test
    public void 매장리스트조회() throws Exception{

    }




}
