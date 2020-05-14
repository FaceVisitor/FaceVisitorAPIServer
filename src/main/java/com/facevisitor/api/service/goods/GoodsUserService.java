package com.facevisitor.api.service.goods;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.common.exception.NotFoundGoodsException;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.repository.GoodsCategoryRepository;
import com.facevisitor.api.repository.GoodsRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GoodsUserService {

    GoodsRepository goodsRepository;

    GoodsCategoryRepository categoryRepository;

    public Goods get(Long goodsId) {
        Goods goods = goodsRepository.get(goodsId).orElseThrow(NotFoundGoodsException::new);
        goods.setViewCnt(goods.getViewCnt()+1);
        return goods;
    }

    @Transactional(readOnly = true)
    public List<Goods> getGoods(List<Long> goodsIds)
    {
        return goodsIds.stream().map(id->goodsRepository.findById(id).orElseThrow(NotFoundGoodsException::new)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Long getGoodsByCategory(){
        Long categoryId = 1L;
        GoodsCategory goodsCategory = categoryRepository.findById(categoryId).orElseThrow(NotFoundException::new);
        List<Long> goods = goodsCategory.getGoods().stream().map(Goods::getId).collect(Collectors.toList());
        Long min = Collections.min(goods);
        Long max = Collections.max(goods);
        if(goods.size() > 0){
            return min + (int) (Math.random() * ((max - min) + 1));
        }else{
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<Goods> initRecommend() {
        PageRequest pageRequest = PageRequest.of(0,7);
        return goodsRepository.findAll(pageRequest).getContent();
    }

    @Transactional(readOnly = true)
    public List<Goods> getPop() {
        PageRequest pageRequest = PageRequest.of(1,15);
        return goodsRepository.getPopularity(pageRequest);
    }

    @Transactional(readOnly = true)
    public List<Goods> initHistory() {
        PageRequest pageRequest = PageRequest.of(5, 7);
        return goodsRepository.findAll(pageRequest).getContent();
    }

    public List<Goods> searchList(String keyword) {
        return goodsRepository.search(keyword);
    }

    public List<GoodsDTO.GoodsListForCSVResponse> all() {
        return goodsRepository.findAll().stream().map(goods -> {
            GoodsDTO.GoodsListForCSVResponse response = new GoodsDTO.GoodsListForCSVResponse();
            response.setCategory(goods.getCategories().stream().findFirst().get().getName());
            response.setId(goods.getId());
            response.setName(goods.getName());
            response.setVendor(goods.getVendor());
            return response;
        }).collect(Collectors.toList());
    }


}
