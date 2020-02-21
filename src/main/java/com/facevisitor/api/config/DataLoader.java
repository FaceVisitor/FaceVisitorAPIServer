package com.facevisitor.api.config;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.config.security.SecurityUserDetailService;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.domain.store.Store;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.repository.AuthorityRepository;
import com.facevisitor.api.repository.GoodsCategoryRepository;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.repository.StoreRepository;
import com.facevisitor.api.service.user.UserService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    SecurityUserDetailService userDetailService;
    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private UserService authService;

    @Autowired
    GoodsRepository goodsRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    GoodsCategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
//            createJsonGoods();

    }

    public void createJsonGoods() throws IOException, ParseException {
        Store store = storeRepository.findById(23L).orElseThrow(NotFoundException::new);
        GoodsCategory goodsCategory = categoryRepository.findById(2L).orElseThrow(NotFoundException::new);
        JSONParser parser = new JSONParser();
        Object parse = parser.parse(new FileReader(new ClassPathResource("static/olive.json").getFile()));
        JSONArray jsonArray = (JSONArray) parse;
        Gson gson = new Gson();
        ArrayList<GoodsDTO.GoodsJsonCreateRequest> o = gson.fromJson(jsonArray.toString(), new TypeToken<ArrayList<GoodsDTO.GoodsJsonCreateRequest>>() {
        }.getType());
        List<Goods> goodsList = o.stream().map(goodsJsonCreateRequest -> {
            String replacedPrice = goodsJsonCreateRequest.getPrice().replace(",", "");
            Goods goods = new Goods();
            goods.setName(goodsJsonCreateRequest.getName());
            goods.setPrice(BigDecimal.valueOf(Long.parseLong(replacedPrice)));
            goods.setVendor(goodsJsonCreateRequest.getBrand());
            goods.setStore(store);
            goods.setCategories(Collections.singleton(goodsCategory));
            goods.setActive(true);
            GoodsImage goodsImage = new GoodsImage();
            goodsImage.setUrl(goodsJsonCreateRequest.getImg());
            goodsImage.setGoods(goods);
            goods.addImage(goodsImage);
            return goods;
        }).collect(Collectors.toList());
        List<Goods> saved = goodsRepository.saveAll(goodsList);
    }

    public void createUser() {

    }

}
