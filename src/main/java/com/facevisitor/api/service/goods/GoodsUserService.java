package com.facevisitor.api.service.goods;

import com.facevisitor.api.common.exception.NotFoundGoodsException;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.repository.GoodsRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsUserService {

    final
    GoodsRepository goodsRepository;

    public GoodsUserService(GoodsRepository goodsRepository) {
        this.goodsRepository = goodsRepository;
    }

    @Transactional(readOnly = true)
    public Goods get(Long id){
        return goodsRepository.get(id).orElseThrow(NotFoundGoodsException::new);
    }
    @Transactional(readOnly = true)
    public List<Goods> initRecommend() {
        PageRequest pageRequest = PageRequest.of(0,7);
        return goodsRepository.findAll(pageRequest).getContent();
    }

    @Transactional(readOnly = true)
    public List<Goods> initBest() {
        PageRequest pageRequest = PageRequest.of(1,7);
        return goodsRepository.findAll(pageRequest).getContent();
    }
    @Transactional(readOnly = true)
    public List<Goods> initHistory() {
        PageRequest pageRequest = PageRequest.of(5,7);
        return goodsRepository.findAll(pageRequest).getContent();
    }
}
