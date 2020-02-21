package com.facevisitor.api.controller;


import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.domain.store.Store;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.dto.image.ImageDto;
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

public class GoodsTest extends BaseTest {

    String baseUrl = "/api/v1/owner/goods";

    String baseCategoryUrl = "/api/v1/owner/goods/category";

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


       mockMvc.perform(postWithUser(baseUrl)
                .content(objectMapper.writeValueAsString(goodsCreateRequest)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.images").isNotEmpty())
                .andExpect(jsonPath("$.categories").isNotEmpty())
                .andDo(print());

    }

    @Test
    public void 상품_페이징() throws Exception {
        mockMvc.perform(getWithUser(baseUrl));
    }

    @Test
    public void 상품_생성() throws Exception {

    }

    @Test
    public void 상품_조회() throws Exception {
        mockMvc.perform(getWithUser(baseUrl + "/" + goods.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.images").exists())
                .andExpect(jsonPath("$.name").value(goods.getName()))
                .andExpect(jsonPath("$._links").hasJsonPath())
                .andDo(print());
    }

    @Test
    public void 상품_수정() throws Exception {
        Goods goods = createGoods();
        goods.setPrice(BigDecimal.valueOf(20000L));
        goods.setName("상품 수정 이름");
        mockMvc.perform(putWithUser(baseUrl + "/" + this.goods.getId()
        ).content(objectMapper.writeValueAsString(goods))).andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.price").value(20000))
                .andExpect(jsonPath("$.name").value("상품 수정 이름"))
                .andDo(print());

    }

    @Test
    public void 상품_삭제() throws Exception {
        mockMvc.perform(deleteWithUser(baseUrl + "/" + goods.getId()
        )).andExpect(status().is2xxSuccessful()).andDo(print());
    }

    @Test
    public void 상품_이미지_추가() throws Exception {
        ImageDto imageDto = new ImageDto();
        imageDto.setUrl("added image url");
        imageDto.setName("image name");
        mockMvc.perform(postWithUser(baseUrl + "/" + goods.getId() + "/image")
                .content(objectMapper.writeValueAsString(imageDto)))
                .andExpect(jsonPath("$.url").value("added image url"))
                .andDo(print());
    }


    @Test
    public void 상품_카테고리_생성() throws Exception {
        mockMvc.perform(postWithUser(baseCategoryUrl).content(objectMapper.writeValueAsString(category)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.name").value(category.getName()))
                .andDo(print());
    }

    @Test
    public void 상품_카테고리_리스트() throws Exception {
        mockMvc.perform(getWithUser(baseCategoryUrl + "/"))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }

    @Test
    public void 상품_카테고리_조회() throws Exception {
        mockMvc.perform(getWithUser(baseCategoryUrl + "/"+category.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andDo(print());
    }

    @Test
    public void 상품_카테고리_수정() throws Exception {

        String oriName = category.getName();
        category.setName("changeName");
        mockMvc.perform(putWithUser(baseCategoryUrl + "/"+category.getId()).content(objectMapper.writeValueAsString(category)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andExpect(jsonPath("$.name").value("changeName"))
                .andDo(print());
        category.setName(oriName);
    }

    @Test
    public void 상품_카테고리_삭제() throws Exception {
        mockMvc.perform(deleteWithUser(baseCategoryUrl + "/"+category.getId()))
                .andExpect(status().is2xxSuccessful());
    }


    public Goods createGoods() {
        GoodsCategory category = new GoodsCategory();
        category.setName("카테고리 이름");
        this.category = categoryRepository.save(category);

        Store store = new Store();
        store.setName("매장이름");
        Store store1 = storeRepository.save(store);
        GoodsImage goodsImage = new GoodsImage();
        goodsImage.setName("이미지 TEST");
        goodsImage.setUrl("image url");
        Goods goods = new Goods();
        goods.setId(1L);
        goods.setName("상품이름");
        goods.setVendor("제조사 이름");
        goods.setPrice(BigDecimal.valueOf(100L));
//        goods.addCategory(category);
        goods.addImage(goodsImage);
        return goods;
    }


}
