package com.facevisitor.api.controller;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.service.goods.GoodsUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/goods")
@Slf4j
public class GoodsController {

    final
    GoodsUserService goodsService;

    public GoodsController(GoodsUserService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("init/recommend")
    public ResponseEntity initRecommend(){
        List<Goods> goods = goodsService.initRecommend();
        return ResponseEntity.ok(goods);
    }

    @GetMapping("init/best")
    public ResponseEntity initBest(){
        List<Goods> goods = goodsService.initBest();
        return ResponseEntity.ok(goods);
    }

    @GetMapping("init/history")
    public ResponseEntity initHistory(){
        List<Goods> goods = goodsService.initHistory();
        return ResponseEntity.ok(goods);
    }


}
