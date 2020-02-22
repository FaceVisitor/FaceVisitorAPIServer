package com.facevisitor.api.controller;


import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.domain.store.Store;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.repository.GoodsCategoryRepository;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.repository.StoreRepository;
import com.facevisitor.api.service.goods.GoodsCategoryService;
import com.facevisitor.api.service.goods.GoodsOwnerService;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GoodsUserTest extends BaseTest {

    String baseOwnerUrl = "/api/v1/owner/goods";

    String baseUrl = "/api/v1/user/goods";

    @Autowired
    GoodsOwnerService goodsOwnerService;

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    GoodsCategoryRepository categoryRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    GoodsCategoryService categoryService;

    GoodsCategory category = null;

    Goods goods = null;

    Store store = null;

    User user = null;

    @Autowired
    ModelMapper modelMapper;

    @Before
    public void setup() throws Exception {
        GoodsDTO.GoodsCreateRequest goodsCreateRequest = new GoodsDTO.GoodsCreateRequest();
        goodsCreateRequest.setActive(true);
        goodsCreateRequest.setDescription("상품설명");
        goodsCreateRequest.setName("상품 이름");
        goodsCreateRequest.setPrice(BigDecimal.TEN);
        GoodsImage goodsImage = new GoodsImage();
        goodsImage.setName("이미지 이름");
        goodsImage.setUrl("이미지 url");
        goodsCreateRequest.setImages(Collections.singleton(goodsImage));


        User user = new User();
        user.setName("테스트 이름");
        user.setEmail("wndudpower@gmail.com");
        user.setPassword("3223");
        this.user = userRepository.save(user);

        Store store = new Store();
        store.setName("매장이름");
        store.setUser(this.user);
        this.store = storeRepository.save(store);
        goodsCreateRequest.setStore(this.store.getId());

        GoodsCategory category = new GoodsCategory();
        category.setName("카테고리 이름");
        this.category = categoryRepository.save(category);
        goodsCreateRequest.setCategory(this.category.getId());

        mockMvc.perform(postWithUser(baseOwnerUrl)
                .content(objectMapper.writeValueAsString(goodsCreateRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.categories").isNotEmpty())
                .andDo(print());

    }

    @Test
    public void 상품_디테일() throws Exception {
        mockMvc.perform(getWithUser(baseUrl + "/" + 1))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1))
                .andDo(print());
    }


}
