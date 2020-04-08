package com.facevisitor.api.owner.controller;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.common.exception.UnAuthorizedException;
import com.facevisitor.api.domain.store.Store;
import com.facevisitor.api.domain.store.StoreImage;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.image.ImageDto;
import com.facevisitor.api.owner.dto.StoreDto;
import com.facevisitor.api.service.base.BaseService;
import com.facevisitor.api.service.store.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Slf4j
@RequestMapping("/api/v1/owner/store")
@AllArgsConstructor
public class StoreController {

    StoreService storeService;

    BaseService baseService;

    ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity list(Principal principal){
        if(principal == null){
            throw new UnAuthorizedException();
        }
        List<Store> list = storeService.list(principal.getName());
        CollectionModel<Store> stores = new CollectionModel<>(list);
        stores.add(linkTo(GoodsCategoryController.class).withSelfRel());
        return ResponseEntity.ok(stores);
    }

    @PostMapping
    public ResponseEntity create(Principal principal,@RequestBody StoreDto.StoreRequest storeRequest){
        Store store = modelMapper.map(storeRequest, Store.class);
        User user = baseService.currentUser(principal.getName());
        store.setUser(user);
        Store created = storeService.create(store);
        return ResponseEntity.created(URI.create("/owner/store")).body(created);
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Long id) {
        if (id != null) {
            return ResponseEntity.ok(storeService.get(id));
        } else {
            throw new BadRequestException();
        }
    }

    @PutMapping("{id}")
    public ResponseEntity update(@RequestBody StoreDto.StoreRequest storeRequest, @PathVariable Long id) {
        storeRequest.setId(id);
        return ResponseEntity.ok(storeService.update(storeRequest));
    }


    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        storeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("{store_id}/image")
    public ResponseEntity addImage(@PathVariable Long store_id , @RequestBody ImageDto imageDto){
        storeService.addImage(store_id,modelMapper.map(imageDto,StoreImage.class));
        return ResponseEntity.ok().build();
    }

    @PostMapping("{store_id}/image/delete")
    public ResponseEntity deleteImage(@PathVariable Long store_id, @RequestBody ImageDto imageDto){
        storeService.deleteImage(store_id,imageDto.getUrl());
        return ResponseEntity.ok().build();
    }
}
