package com.facevisitor.api;

import com.facevisitor.api.dto.user.Join;
import com.facevisitor.api.dto.user.Login;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthTest extends BaseTest {
    @Value("${secret.oauth.clientId}")
    String clientId;

    @Value("${secret.oauth.clientSecurity}")
    String clientSecurity;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAuthToken() throws Exception {
        mockMvc.perform(
                post("/oauth/token")
                        .with(httpBasic(clientId, clientSecurity))
                        .param("username", "sajang@facevisitor.com")
                        .param("password", "asdf4112")
                        .param("grant_type", "password")
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists())
                .andDo(print());
    }


    @Test
    public void 회원가입() throws Exception {
        Join join = new Join();
        join.setEmail("test.com");
        join.setPassword("asdf4112");
        join.setLowAge(22);
        join.setHighAge(24);
        join.setGender("Male");
        join.setFaceIds(Arrays.asList("testface1", "testface2"));
        join.setFaceImageUrl(Arrays.asList("url1", "url2"));
        join.setPhone("01026588178");
        mockMvc.perform(
                post("/api/v1/auth/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(join)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void 다이렉트_로그인() throws Exception {
        Login.Request request = new Login.Request();
        request.setFaceId(Arrays.asList("495aa180-55ac-4c06-be15-e0b45366b1ec"));
        mockMvc.perform(
                post("/api/v1/auth/direct_login").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andDo(print());
    }

    @Test
    public void 로그인() throws Exception {
        Login.Request request = new Login.Request();
        request.setFaceId(Arrays.asList("495aa180-55ac-4c06-be15-e0b45366b1ec"));
        mockMvc.perform(
                post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(jsonPath("$.refresh_token").exists())
                .andExpect(jsonPath("$.createdAt").exists())
                .andDo(print());
    }


//    @Test
//    public void refresh_token() throws Exception {
//        Login login = new Login();
//        login.setEmail("wndudpower@gmail.com");
//        login.setPassword("asdf4112");
//        mockMvc.perform(
//                post("/api/v1/auth/refresh_token").content(new ObjectMapper().writeValueAsString(login)))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(jsonPath("$.access_token").exists())
//                .andExpect(jsonPath("$.refresh_token").exists())
//                .andDo(print());
//        login.setEmail("wndudpower@gmail.com");
//    }

}
