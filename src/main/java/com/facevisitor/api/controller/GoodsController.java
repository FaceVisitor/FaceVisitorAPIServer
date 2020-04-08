package com.facevisitor.api.controller;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.service.goods.GoodsUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/goods")
@Slf4j
@AllArgsConstructor
public class GoodsController {

    GoodsUserService goodsService;

    ModelMapper modelMapper;

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
    public ResponseEntity initHistory() {
        List<Goods> goods = goodsService.initHistory();
        return ResponseEntity.ok(goods);
    }

    @PostMapping("json")
    public ResponseEntity initJson() {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity all() {
        return ResponseEntity.ok(goodsService.all());
    }


}
