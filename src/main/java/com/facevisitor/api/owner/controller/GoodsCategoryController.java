package com.facevisitor.api.owner.controller;

import com.facevisitor.api.domain.goods.GoodsCategory;
import com.facevisitor.api.resource.GoodsCategoryResource;
import com.facevisitor.api.service.goods.GoodsCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Slf4j
@RequestMapping("/api/v1/owner/goods/category")
public class GoodsCategoryController {

    final GoodsCategoryService categoryService;

    final
    PagedResourcesAssembler pagedResourcesAssembler;

    final
    ModelMapper modelMapper;

    public GoodsCategoryController(GoodsCategoryService categoryService, PagedResourcesAssembler pagedResourcesAssembler, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity list() {
        List<GoodsCategory> list = categoryService.list();
        CollectionModel<GoodsCategory> categories = new CollectionModel<>(list);
        categories.add(linkTo(GoodsCategoryController.class).withSelfRel());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity detail(@PathVariable Long id) {
        GoodsCategory detail = categoryService.get(id);
        GoodsCategoryResource resource = new GoodsCategoryResource(detail);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody GoodsCategory category) {
        GoodsCategory created = categoryService.create(category);
        URI createdURI = linkTo(GoodsCategoryController.class).slash(created.getId()).toUri();
        GoodsCategoryResource resource = new GoodsCategoryResource(created);
        return ResponseEntity.created(createdURI).body(resource);
    }


    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody GoodsCategory toCategory) {
        GoodsCategory update = categoryService.update(id, toCategory);
        GoodsCategoryResource resource = new GoodsCategoryResource(update);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }

}
