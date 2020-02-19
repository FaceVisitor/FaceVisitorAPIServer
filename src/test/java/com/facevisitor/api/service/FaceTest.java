package com.facevisitor.api.service;

import com.facevisitor.api.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FaceTest {

    @Autowired
    UserRepository userRepository;



    @Test
    @Transactional
    public void faceMeta생성() {


//        User user = new User();
//        user.setName("ee");
//        FaceMeta faceMeta = new FaceMeta();
//        faceMeta.setGender("Male");
//        faceMeta.setHighAge(22);
//        faceMeta.setLowAge(10);
//        FaceId faceId = new FaceId("aa",faceMeta);
//        FaceImage faceImage = new FaceImage();
//        faceImage.setUrl("testurl");
//        faceMeta.setFaceId(Arrays.asList(faceId));
//        faceMeta.setFaceImages(Arrays.asList(faceImage));
//        user.addFaceMeta(faceMeta);
//
//        userRepository.save(user);
    }
}
