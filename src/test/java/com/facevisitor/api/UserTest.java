package com.facevisitor.api;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.repository.GoodsRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserTest extends BaseTest {
    String baseUrl = "/api/v1/user/";

    @Autowired
    GoodsRepository goodsRepository;


    @Test
    public void 유저중복체크() throws Exception {
        User owner = createOwner();
        HashMap<String, String> email = new HashMap<>();
        email.put("email", owner.getEmail());
        mockMvc.perform(
                post("/api/v1/user/exist").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(email)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void 유저정보() throws Exception {
        mockMvc.perform(
                postWithUser(baseUrl + "me"))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void 상품_좋아요() throws Exception {
        //set
        User user = createOwner();
        Goods goods = new Goods();
        goods.setName("test");
        Goods savedGoods = goodsRepository.save(goods);

        //when
        mockMvc.perform(
                postWithUser(baseUrl + "/like/goods/" + savedGoods.getId()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        //then
        System.out.println(user.getGoodsLike());
        assertThat(user.getGoodsLike()).isNotEmpty();
    }

    @Test
    public void 상품_좋아요_취소() throws Exception {
        //set
        User user = createOwner();
        Goods goods = new Goods();
        goods.setName("test");
        Goods savedGoods = goodsRepository.save(goods);

        //when

        //좋아요
        mockMvc.perform(
                postWithUser(baseUrl + "/like/goods/" + savedGoods.getId()))
                .andExpect(status().is2xxSuccessful());
        Set<Goods> goodsLike = user.getGoodsLike();
        assertThat(goodsLike).isNotEmpty();

        //취소
        mockMvc.perform(
                postWithUser(baseUrl + "/like/goods/" + savedGoods.getId()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());

        //then
        assertThat(goodsLike).isEmpty();
        System.out.println(goodsLike);
    }

}
