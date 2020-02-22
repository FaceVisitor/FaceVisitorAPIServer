package com.facevisitor.api.controller;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.service.goods.GoodsUserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/goods")
@Slf4j
public class GoodsController {

    final
    GoodsUserService goodsService;

    @Autowired
    ModelMapper modelMapper;

    public GoodsController(GoodsUserService goodsService) {
        this.goodsService = goodsService;
    }

    @GetMapping("{goods_id}")
    public ResponseEntity get(@PathVariable  Long goods_id){
            return ResponseEntity.ok(modelMapper.map(goodsService.get(goods_id), GoodsDTO.GoodsUserDetailResponse.class));
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
