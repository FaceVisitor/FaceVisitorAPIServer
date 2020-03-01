package com.facevisitor.api.config;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.config.security.SecurityUserDetailService;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.domain.security.Authority;
import com.facevisitor.api.domain.store.Store;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.owner.dto.auth.OJoin;
import com.facevisitor.api.owner.service.OauthService;
import com.facevisitor.api.repository.*;
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
    OauthService oauthService;

    @Autowired
    GoodsCategoryRepository categoryRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
//            createOwner();
            createJsonGoods(1L,1L);
    }

    String email = "sajang@facevisitor.com";
    String password = "asdf4112";
    String name = "일반유저";

    @Autowired
    protected UserRepository userRepository;


    public void createJsonGoods(Long storeId,Long goodsCategoryId) throws IOException, ParseException {
        Store store = storeRepository.findById(storeId).orElseThrow(NotFoundException::new);
        GoodsCategory goodsCategory = categoryRepository.findById(goodsCategoryId).orElseThrow(NotFoundException::new);
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

    public Authority createOwnerAuthority(){
        if (!authorityRepository.findByRole("OWNER").isPresent()) {
            Authority authority = new Authority();
            authority.setRole("OWNER");
            return authorityRepository.save(authority);
        } else {
            return authorityRepository.findByRole("OWNER").orElseThrow(NotFoundException::new);
        }
    }

    public User createOwner() throws Exception {
        createOwnerAuthority();
        if (!userRepository.findByEmail(email).isPresent()) {
            OJoin join = new OJoin();
            join.setEmail(email);
            join.setPassword(password);
            join.setName("허주영 사장");
            join.setPhone("01026588178");
            return oauthService.join(join);
        } else {
            return userRepository.findByEmail(email).get();
        }
    }

}
