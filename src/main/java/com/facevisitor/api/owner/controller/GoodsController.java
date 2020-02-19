package com.facevisitor.api.owner.controller;

import com.facevisitor.api.domain.goods.Goods;
import com.facevisitor.api.domain.goods.GoodsImage;
import com.facevisitor.api.dto.image.ImageDto;
import com.facevisitor.api.resource.GoodsResource;
import com.facevisitor.api.service.goods.GoodsService;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


@RestController
@RequestMapping("/api/v1/owner/goods")
@Slf4j
public class GoodsController {

    final GoodsService goodsService;

    final
    PagedResourcesAssembler pagedResourcesAssembler;

    final
    ModelMapper modelMapper;

    public GoodsController(GoodsService goodsService, PagedResourcesAssembler pagedResourcesAssembler, ModelMapper modelMapper) {
        this.goodsService = goodsService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity page(
            @PageableDefault(size = 20, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String searchQuery
    ) {

        Page<Goods> page = goodsService.page(pageable, searchQuery);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        Goods detail = goodsService.get(id);
        GoodsResource goodsResource = new GoodsResource(detail);
        return ResponseEntity.ok(goodsResource);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody Goods goods) {
        Goods created = goodsService.create(goods);
        URI createdURI = linkTo(GoodsController.class).slash(created.getId()).toUri();
        GoodsResource goodsResource = new GoodsResource(created);
        return ResponseEntity.created(createdURI).body(goodsResource);
    }


    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody Goods toGoods) {
        Goods update = goodsService.update(id, toGoods);
        GoodsResource goodsResource = new GoodsResource(update);
        return ResponseEntity.ok(goodsResource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        goodsService.get(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{id}/image")
    public ResponseEntity addImage(@PathVariable Long id, @RequestBody ImageDto imageDto) {
        GoodsImage image = modelMapper.map(imageDto, GoodsImage.class);
        goodsService.addImage(id, image);
        return ResponseEntity.ok(image);
    }

    @PostMapping("{id}/image/delete")
    public ResponseEntity deleteImage(@PathVariable Long id, @RequestBody ImageDto imageDto) {
        goodsService.deleteImage(id, imageDto.getUrl());
        return ResponseEntity.ok().build();
    }


}
