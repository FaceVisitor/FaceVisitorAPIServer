package com.facevisitor.api.service;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.store.Store;
import com.facevisitor.api.domain.store.StoreImage;
import com.facevisitor.api.domain.store.StoreRepository;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.repo.UserRepository;
import com.facevisitor.api.owner.dto.StoreDto;
import com.facevisitor.api.service.store.StoreService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class StoreTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    StoreService storeService;


    @Test
    @Transactional
    public void create(){
        Store store = createStore();
        assertThat(store.getName(),is("test"));
    }


    @Test
    public void list() throws Exception{
        String email = "sajang@facevisitor.com";
        List<Store> list = storeService.list(email);
        System.out.println(list);
    }

    @Test
    public void get() throws Exception{
        Store store = storeService.get(5L);
        assertThat(store.getId(),is(5L));
        System.out.println(store);
    }

    @Test
    @Transactional
    public void update() throws Exception{
        StoreDto.StoreRequest updated = new StoreDto.StoreRequest();
        updated.setAddress("updated address");
        updated.setName("updated Name");
        updated.setDescription("updated description");
        Store update = storeService.update(updated);
        System.out.println(update);
    }

    @Test
    @Transactional
    public void delete() throws Exception{
        storeService.delete(5L);
    }

    @Test
    @Transactional
    public void addImage(){
        StoreImage storeImage = new StoreImage();
        storeImage.setUrl("test url");
        storeImage.setName("test name");
        storeService.addImage(5L, storeImage);

    }


    @Test
    @Transactional
    public void deleteImage() throws Exception{
        storeService.deleteImage(5L,"image url");
        List<StoreImage> images = storeService.get(5L).getImages();
        System.out.println(images);
    }


    public Store createStore(){
        Store store = new Store();
        store.setName("test");
        store.setAddress("address");
        store.setDescription("desc");
        StoreImage storeImage = new StoreImage();
        storeImage.setUrl("image url");
        store.addImages(Arrays.asList(storeImage));
        User user = userRepository.findByEmail("sajang@facevisitor.com").orElseThrow(NotFoundException::new);
        store.setUser(user);
        Store created = storeService.create(store);
        return created;
    }

}
