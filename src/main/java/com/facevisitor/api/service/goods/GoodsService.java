package com.facevisitor.api.service.goods;

import com.facevisitor.api.common.exception.NotFoundGoodsException;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.service.file.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GoodsService {

  final GoodsRepository goodsRepository;

  @Autowired
  FileService fileService;


  public GoodsService(GoodsRepository goodsRepository) {
    this.goodsRepository = goodsRepository;
  }


  public Page<Goods> page(Pageable pageable,String searchQuery){
    return goodsRepository.page(searchQuery,pageable);
  }

  public Goods get(Long id){
    return goodsRepository.findById(id).orElseThrow(()-> new NotFoundGoodsException(id));
  }

  public Goods create(Goods goods){
    return goodsRepository.save(goods);
  }

  public void delete(Long id){
    Goods goods = goodsRepository.findById(id).orElseThrow(() -> new NotFoundGoodsException(id));
    goodsRepository.delete(goods);
  }

  public Goods update(Long id, Goods fromGoods){
    Goods toGoods = get(id);
    toGoods.setName(fromGoods.getName());
    toGoods.setPrice(fromGoods.getPrice());
    toGoods.setVendor(fromGoods.getVendor());
    toGoods.setSalePrice(fromGoods.getSalePrice());
    return toGoods;
  }

  public void addImage(Long id, GoodsImage goodsImage){
    Goods goods = get(id);
    goods.addImage(goodsImage);
    goodsRepository.save(goods);
  }

  public void deleteImage(Long id, String url) {
    Goods goods = get(id);
    goods.deleteImage(url);
    fileService.deleteS3(url);
  }


}
