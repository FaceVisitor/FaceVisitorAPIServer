package com.facevisitor.api;

import com.facevisitor.api.domain.cart.Cart;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.repository.GoodsRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartTest extends BaseTest {

    @Autowired
    GoodsRepository goodsRepository;

    String baseUrl = "/api/v1/cart";

    Cart cart = null;


    @Before
    public void setup() throws Exception {
        createTest();

    }

    @Test
    public void 목록() throws Exception {
        mockMvc.perform(getWithUser(baseUrl))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }


    @Test
    public void 수량변경() throws Exception {
        mockMvc.perform(putWithUser(baseUrl + "/" + this.cart.getId() + "/" + 2))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.qty").value(2))
                .andDo(print());
    }

    @Test
    public void 삭제() throws Exception {
        mockMvc.perform(deleteWithUser(baseUrl + "/" + this.cart.getId()))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void 모두삭제() throws Exception {
        mockMvc.perform(deleteWithUser(baseUrl + "/all"))
                .andExpect(status().is2xxSuccessful());
    }


    public void createTest() throws Exception {
        User user = createOwner();
        Goods goods = new Goods();
        goods.setName("test");
        goods.setPrice(BigDecimal.TEN);
        Goods savedGoods = goodsRepository.save(goods);
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setQty(1);
        cart.setGoods(savedGoods);
        this.cart = gson.fromJson(mockMvc.perform(postWithUser(baseUrl)
                .content(objectMapper.writeValueAsString(cart)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.goods").exists())
                .andDo(print()).andReturn().getResponse().getContentAsString(), Cart.class);

    }
}
