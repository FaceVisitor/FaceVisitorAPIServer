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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/api/v1/owner/store")
public class StoreController {

    @Autowired
    StoreService storeService;

    @Autowired
    BaseService baseService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity list(Principal principal){
        if(principal == null){
            throw new UnAuthorizedException();
        }
        List<Store> list = storeService.list(principal.getName());
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity create(Principal principal,@RequestBody StoreDto.StoreRequest storeRequest){
        Store map = modelMapper.map(storeRequest, Store.class);
        List<StoreImage> images = storeRequest.getImages().stream().map(imageDto -> modelMapper.map(imageDto, StoreImage.class)).collect(Collectors.toList());
        map.getImages().clear();
        map.addImages(images);
        User user = baseService.currentUser(principal.getName());
        map.setUser(user);
        Store created = storeService.create(map);
        return ResponseEntity.created(URI.create("/owner/store")).body(created);
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable Long id){
        if(id != null){
            return ResponseEntity.ok(storeService.get(id));
        }else{
            throw new BadRequestException();
        }
    }

    @PutMapping
    public ResponseEntity update(@RequestBody StoreDto.StoreRequest storeRequest){
        return ResponseEntity.ok(storeService.update(storeRequest));
    }


    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable Long id){
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
