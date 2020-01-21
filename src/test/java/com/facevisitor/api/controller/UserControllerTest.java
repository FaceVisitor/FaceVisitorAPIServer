package com.facevisitor.api.controller;

import com.facevisitor.api.dto.user.Login;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void 유저중복체크() throws Exception {
        HashMap<String,String> email = new HashMap<>();
        email.put("email","wndudpower@gmail.com");
        mockMvc.perform(
                post("/api/v1/user/exist").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(email)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void 유저정보with토큰() throws Exception{
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJleHAiOjE1Nzk4ODE5ODYsImp0aSI6ImQ3NGMxYjI2LWU4ZWYtNDY3OS1hMTJmLTJhYWE5NmQ4MjY5YSIsImNsaWVudF9pZCI6Imdqd25kdWQiLCJ1c2VyX25hbWUiOiJ3bmR1ZHBvd2VyQGdtYWlsLmNvbSIsInNjb3BlIjpbInJlYWQiXX0.Sc9nulsDgDnJXWa3w49kjHYx87K0R5Iz05FNQbKA7by5A6owaNcSQeOhFiJOmiuWP4wkJJCxVXVWWdqwujLXQQQVRvP1y8jFrkwPy9CuUF7282HodIAvx6HOE6RSrSjc9jAX1h5niotfetUAup3rBQIjyaKRUZwyjSKtKH6XNEmw4VMPNsWkXyfu7Gs0OFQ9C4C-rw2O5bYrpoJj7_CQqo4Wtx2sNKj3dPiNnRa4AnWjkPLnGnYqw4frgKpZHth19n0x7yuOG2ud8PxaL5jdfETegpb6JEqAqWDv5ZC5GNKvanglcnOntRUibfmIhFOd_BYmz0krgzavck1W-3eHgw";
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/user/me").header("Authorization", "bearer " + token))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

}
