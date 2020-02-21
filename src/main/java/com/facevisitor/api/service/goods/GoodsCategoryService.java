package com.facevisitor.api.service.goods;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.repository.GoodsCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GoodsCategoryService {

    final GoodsCategoryRepository categoryRepository;


    public GoodsCategoryService(GoodsCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public GoodsCategory create(GoodsCategory goodsCategory){
        return categoryRepository.save(goodsCategory);
    }

    public GoodsCategory get(Long id){
        return categoryRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public List<GoodsCategory> list(){
        return categoryRepository.findAll();
    }

    public GoodsCategory update(Long id, GoodsCategory from){
        GoodsCategory to = get(id);
        to.setName(from.getName());
        to.setActive(from.getActive());
        return categoryRepository.save(to);
    }

    public void delete(Long id){
        GoodsCategory goodsCategory = get(id);
        categoryRepository.delete(goodsCategory);
    }


}
