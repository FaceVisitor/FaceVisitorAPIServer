package com.facevisitor.api.service;

import com.facevisitor.api.BaseTest;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.repository.GoodsRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JsonTest extends BaseTest {


    @Autowired
    ModelMapper modelMapper;

    @Autowired
    GoodsRepository goodsRepository;

    @Test
    public void test() throws Exception {
        JSONParser parser = new JSONParser();
        Object parse = parser.parse(new FileReader(new ClassPathResource("static/olive.json").getFile()));
        JSONArray jsonArray = (JSONArray) parse;
        Gson gson = new Gson();
        ArrayList<GoodsDTO.GoodsJsonCreateRequest> o = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<GoodsDTO.GoodsJsonCreateRequest>>() {}.getType());
        List<Goods> goodsList = o.stream().map(goodsJsonCreateRequest -> {
            String replacedPrice = goodsJsonCreateRequest.getPrice().replace(",", "");
            Goods goods = new Goods();
            goods.setName(goodsJsonCreateRequest.getName());
            goods.setPrice(BigDecimal.valueOf(Long.parseLong(replacedPrice)));
            goods.setVendor(goodsJsonCreateRequest.getBrand());
            goods.setActive(true);
            GoodsImage goodsImage = new GoodsImage();
            goodsImage.setUrl(goodsJsonCreateRequest.getImg());
            goods.setImages(Collections.singleton(goodsImage));
            return goods;
        }).collect(Collectors.toList());
        List<Goods> saved = goodsRepository.saveAll(goodsList);



//        mockMvc.perform(postWithUser("/api/v1/owner/goods").content(objectMapper.writeValueAsString(collect))).andDo(print());


    }
}
