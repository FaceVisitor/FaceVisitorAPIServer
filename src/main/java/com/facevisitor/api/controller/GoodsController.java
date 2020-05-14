package com.facevisitor.api.controller;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.service.goods.GoodsHistoryService;
import com.facevisitor.api.service.goods.GoodsUserService;
import com.facevisitor.api.service.personalize.PersonalizeService;
import com.facevisitor.api.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/goods")
@Slf4j
@AllArgsConstructor
public class GoodsController {

    GoodsUserService goodsService;

    UserService userService;

    ModelMapper modelMapper;

    PersonalizeService personalizeService;

    GoodsHistoryService goodsHistoryService;

    @GetMapping("{goodsId}")
    public ResponseEntity detail(Principal principal, @PathVariable Long goodsId) throws JsonProcessingException {

        User userByEmail = userService.getUserByEmail(principal.getName());
        Goods detail = goodsService.get(goodsId);
        //조회 추천 이벤트 발생
        //상품 기록
        goodsHistoryService.addGoodsHistory(userByEmail, detail);
        GoodsDTO.GoodsDetailResponse detailResponse = modelMapper.map(detail, GoodsDTO.GoodsDetailResponse.class);
        if (detail.getCategories() != null && detail.getCategories().size() > 0) {
            detailResponse.setCategory(detail.getCategories().stream().findFirst().orElse(null));
        }
        return ResponseEntity.ok(detailResponse);
    }

    @GetMapping("/pop")
    public ResponseEntity getPop() {
        return ResponseEntity.ok(goodsService.getPop());
    }

    @GetMapping("init/recommend")
    public ResponseEntity initRecommend() {
        List<Goods> goods = goodsService.initRecommend();
        return ResponseEntity.ok(goods);
    }

    @PostMapping("getGoods")
    public ResponseEntity getGoods(@RequestBody HashMap<String,List<Long>> payload) {
        List<Goods> goods = goodsService.getGoods(payload.get("goodsIds"));
        return ResponseEntity.ok(goods);
    }

    @GetMapping("/goods-by-category")
    public ResponseEntity getGoodsByCategory() {
        return ResponseEntity.ok( goodsService.getGoodsByCategory());
    }


    @GetMapping("init/history")
    public ResponseEntity initHistory() {
        List<Goods> goods = goodsService.initHistory();
        return ResponseEntity.ok(goods);
    }


    @GetMapping
    public ResponseEntity all() {
        return ResponseEntity.ok(goodsService.all());
    }

    @GetMapping("search")
    public ResponseEntity search(@RequestParam String keyword) {
        return ResponseEntity.ok(goodsService.searchList(keyword));
    }

    public Long getUserIdByPrincipal(Principal principal) {
        return userService.getUserByEmail(principal.getName()).getId();
    }
}
