package com.facevisitor.api.controller;


import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.dto.image.ImageDto;
import com.facevisitor.api.repository.GoodsCategoryRepository;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.service.goods.GoodsCategoryService;
import com.facevisitor.api.service.goods.GoodsService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GoodsTest extends BaseTest {

    String baseUrl = "/api/v1/goods";

    String baseCategoryUrl = "/api/v1/goods/category";

    @Autowired
    GoodsService goodsService;

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    GoodsCategoryRepository categoryRepository;

    @Autowired
    GoodsCategoryService categoryService;

    GoodsCategory category = null;

    Goods goods = null;

    @Before
    public void before() {
        category = createCategory();
        goods = createGoods();
    }

    @Test
    public void 상품_페이징() throws Exception {
        mockMvc.perform(getWithUser(baseUrl));
    }

    @Test
    public void 상품_생성() throws Exception {
        mockMvc.perform(postWithUser(baseUrl)
                .content(objectMapper.writeValueAsString(goods)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.images").exists())
                .andExpect(jsonPath("$.categories").exists())
                .andDo(print());
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
        mockMvc.perform(postWithUser(baseCategoryUrl + "/").content(objectMapper.writeValueAsString(category)))
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
        mockMvc.perform(putWithUser(baseCategoryUrl + "/"+category.getId()).content(objectMapper.writeValueAsString(category)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andDo(print());

    }

    @Test
    public void 상품_카테고리_삭제() throws Exception {
        mockMvc.perform(deleteWithUser(baseCategoryUrl + "/"+category.getId()))
                .andExpect(status().is2xxSuccessful());
    }

    public GoodsCategory createCategory() {
        GoodsCategory goodsCategory = new GoodsCategory();
        goodsCategory.setName("카테고리 이름");
        return categoryService.create(goodsCategory);
    }

    public Goods createGoods() {
        GoodsCategory category = createCategory();
        GoodsImage goodsImage = new GoodsImage();
        goodsImage.setName("이미지 TEST");
        goodsImage.setUrl("image url");
        Goods goods = new Goods();
        goods.setId(1L);
        goods.setName("상품이름");
        goods.setVendor("제조사 이름");
        goods.setPrice(BigDecimal.valueOf(100L));
        goods.addCategory(category);
        goods.setImages(Collections.singleton(goodsImage));
        return goodsService.create(goods);
    }


}
