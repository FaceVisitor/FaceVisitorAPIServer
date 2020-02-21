package com.facevisitor.api.owner.controller;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.dto.goods.GoodsDTO;
import com.facevisitor.api.dto.image.ImageDto;
import com.facevisitor.api.resource.GoodsResource;
import com.facevisitor.api.service.goods.GoodsOwnerService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping("/api/v1/owner/goods")
@Slf4j
public class GoodsOwnerController {

    final GoodsOwnerService goodsOwnerService;

    final
    PagedResourcesAssembler pagedResourcesAssembler;

    final
    ModelMapper modelMapper;

    public GoodsOwnerController(GoodsOwnerService goodsOwnerService, PagedResourcesAssembler pagedResourcesAssembler, ModelMapper modelMapper) {
        this.goodsOwnerService = goodsOwnerService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity page(
            @PageableDefault(size = 20, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String searchQuery
    ) {
        Page<Goods> page = goodsOwnerService.page(pageable, searchQuery);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Long id) {
        Goods detail = goodsOwnerService.get(id);
        GoodsDTO.GoodsDetailResponse detailResponse = modelMapper.map(detail, GoodsDTO.GoodsDetailResponse.class);
        if(detail.getCategories() != null && detail.getCategories().size() >0) {
            System.out.println(detail.getCategories());
            detailResponse.setCategory(detail.getCategories().stream().findFirst().orElse(null));
        }
        return ResponseEntity.ok(detailResponse);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody GoodsDTO.GoodsCreateRequest goodsReq) {
        Goods goods = new Goods();
        goods.setName(goodsReq.getName());
        goods.setPrice(goodsReq.getPrice());
        goods.setSalePrice(goodsReq.getSalePrice());
        goods.setVendor(goodsReq.getVendor());
        goods.setActive(true);
        goods.setDescription(goodsReq.getDescription());
        goods.setImages(goodsReq.getImages());
        Goods created = goodsOwnerService.create(goods,goodsReq.getCategory(),goodsReq.getStore());
        URI createdURI = linkTo(GoodsOwnerController.class).slash(created.getId()).toUri();
        GoodsResource goodsResource = new GoodsResource(created);
        return ResponseEntity.created(createdURI).body(goodsResource);
    }

    @PostMapping("/json")
    public ResponseEntity createJson(@RequestBody List<GoodsDTO.GoodsCreateRequest> goodsReq, @RequestBody HashMap<String,Long> payload) {

        List<Goods> goodList = goodsReq.stream().map(goodsCreateRequest -> {
            Goods goods = new Goods();
            goods.setName(goodsCreateRequest.getName());
            goods.setPrice(goodsCreateRequest.getPrice());
            goods.setVendor(goodsCreateRequest.getVendor());
            goods.setActive(true);
            goods.setImages(goodsCreateRequest.getImages());
            return goods;
        }).collect(Collectors.toList());
        goodsOwnerService.createByJson(goodList,payload.get("category"),payload.get("store"));
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody GoodsDTO.GoodsUpdateRequest updateRequest) {
        Goods update = goodsOwnerService.update(id, updateRequest);
        GoodsResource goodsResource = new GoodsResource(update);
        return ResponseEntity.ok(goodsResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        goodsOwnerService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/image")
    public ResponseEntity addImage(@PathVariable Long id, @RequestBody ImageDto imageDto) {
        GoodsImage image = modelMapper.map(imageDto, GoodsImage.class);
        goodsOwnerService.addImage(id, image);
        return ResponseEntity.ok(image);
    }

    @PostMapping("{id}/image/delete")
    public ResponseEntity deleteImage(@PathVariable Long id, @RequestBody ImageDto imageDto) {
        goodsOwnerService.deleteImage(id, imageDto.getUrl());
        return ResponseEntity.ok().build();
    }


}
