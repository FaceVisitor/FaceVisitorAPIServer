package com.facevisitor.api.service.goods;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.common.exception.NotFoundGoodsException;
import com.facevisitor.api.common.exception.NotFoundStoreException;
import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.domain.store.Store;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.repository.GoodsCategoryRepository;
import com.facevisitor.api.repository.GoodsRepository;
import com.facevisitor.api.repository.StoreRepository;
import com.facevisitor.api.service.file.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class GoodsOwnerService {

  final GoodsRepository goodsRepository;

  final
  StoreRepository storeRepository;

  final
  GoodsCategoryRepository categoryRepository;

  final
  FileService fileService;


  public GoodsOwnerService(GoodsRepository goodsRepository, StoreRepository storeRepository, FileService fileService, GoodsCategoryRepository categoryRepository) {
    this.goodsRepository = goodsRepository;
    this.storeRepository = storeRepository;
    this.fileService = fileService;
    this.categoryRepository = categoryRepository;
  }

  @Transactional(readOnly = true)
  public Page<Goods> page(Pageable pageable,String searchQuery){
    return goodsRepository.page(searchQuery,pageable);
  }
  @Transactional(readOnly = true)
  public Goods get(Long id){
    return goodsRepository.get(id).orElseThrow(()-> new NotFoundGoodsException(id));
  }

  public Goods create(Goods goods,Long categoryId, Long storeId){
    Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundStoreException(storeId));
    goods.setStore(store);

    GoodsCategory goodsCategory = categoryRepository.findById(categoryId).orElseThrow(NotFoundException::new);
    goods.addCategory(goodsCategory);

    goods.getImages().forEach(goodsImage -> {
      goodsImage.setGoods(goods);
    });
    return goodsRepository.save(goods);
  }

  public void createByJson(List<Goods> goods, Long categoryId, Long storeId){
    Store store = storeRepository.findById(storeId).orElseThrow(() -> new NotFoundStoreException(storeId));
    GoodsCategory goodsCategory = categoryRepository.findById(categoryId).orElseThrow(NotFoundException::new);

    goods.forEach(goodsItem -> {
      goodsItem.getImages().forEach(goodsImage -> {
        goodsImage.setGoods(goodsItem);
      });
      goodsItem.setStore(store);
      goodsItem.setCategories(Collections.singleton(goodsCategory));
    });
    goodsRepository.saveAll(goods);
  }

  public void delete(Long id){
    Goods goods = goodsRepository.findById(id).orElseThrow(() -> new NotFoundGoodsException(id));
    goodsRepository.delete(goods);
  }

  public Goods update(Long id, GoodsDTO.GoodsUpdateRequest fromGoods){
    Goods toGoods = get(id);
    toGoods.setName(fromGoods.getName());
    toGoods.setPrice(fromGoods.getPrice());
    toGoods.setVendor(fromGoods.getVendor());
    toGoods.setSalePrice(fromGoods.getSalePrice());
    toGoods.setActive(fromGoods.getActive());
    Store store = storeRepository.findById(fromGoods.getStore()).orElseThrow(()->new NotFoundStoreException(fromGoods.getStore()));
    toGoods.setStore(store);
    GoodsCategory goodsCategory = categoryRepository.findById(fromGoods.getCategory()).orElseThrow(NotFoundException::new);
    toGoods.getCategories().clear();
    toGoods.addCategory(goodsCategory);
    return toGoods;
  }

  public void addImage(Long id, GoodsImage goodsImage){
    Goods goods = get(id);
    goods.addImage(goodsImage);
  }

  public void deleteImage(Long id, String url) {
    Goods goods = get(id);
    goods.deleteImage(url);
    fileService.deleteS3(url);
  }


}
