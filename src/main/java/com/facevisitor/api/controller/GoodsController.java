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
@RequestMapping("/api/v1/goods")
@Slf4j
public class GoodsController {

    final
    GoodsUserService goodsService;

    @Autowired
    ModelMapper modelMapper;

    public GoodsController(GoodsUserService goodsService) {
        this.goodsService = goodsService;
    }


    @GetMapping("{id}")
    public ResponseEntity detail(@PathVariable Long id){
        Goods detail = goodsService.get(id);
        GoodsDTO.GoodsDetailResponse detailResponse = modelMapper.map(detail, GoodsDTO.GoodsDetailResponse.class);
        if(detail.getCategories() != null && detail.getCategories().size() >0) {
            detailResponse.setCategory(detail.getCategories().stream().findFirst().orElse(null));
        }
        return ResponseEntity.ok(detailResponse);
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
