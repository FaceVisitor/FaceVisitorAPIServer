package com.facevisitor.api.service.store;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.store.Store;
import com.facevisitor.api.domain.store.StoreImage;
import com.facevisitor.api.owner.dto.StoreDto;
import com.facevisitor.api.repository.StoreRepository;
import com.facevisitor.api.service.file.FileService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class StoreService {

    final
    StoreRepository storeRepository;



    final
    ModelMapper modelMapper;

    final
    FileService fileService;

    public StoreService(StoreRepository storeRepository, ModelMapper modelMapper, FileService fileService) {
        this.storeRepository = storeRepository;
        this.modelMapper = modelMapper;
        this.fileService = fileService;
    }

    public List<Store> list(String email){
        return storeRepository.findByUserEmail(email);
    }

    public Store create(Store store){
        return storeRepository.save(store);
    }

    @Transactional(readOnly = true)
    public Store get(Long id){
        return storeRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public Store update(StoreDto.StoreRequest updated){
        Store store = this.get(updated.getId());
        store.setName(updated.getName());
        store.setDescription(updated.getDescription());
        store.setAddress(updated.getAddress());
        store.setOpenTime(updated.getOpenTime());
        store.setPhone(updated.getPhone());
        return store;
    }

    public void delete(Long id){
        Store store = storeRepository.findById(id).orElseThrow(NotFoundException::new);
        store.getImages().forEach(storeImage -> {
            fileService.deleteS3(storeImage.getUrl());
        });
        storeRepository.delete(store);
    }

    public void addImage(Long store_id, StoreImage storeImage){
        Store store = get(store_id);
        store.addImages(Collections.singletonList(storeImage));
        storeRepository.save(store);
    }

    public void deleteImage(Long store_id, String url) {
        Store store = get(store_id);
        store.deleteImage(url);
        fileService.deleteS3(url);
    }
}
