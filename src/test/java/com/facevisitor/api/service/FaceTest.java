package com.facevisitor.api.service;

import com.facevisitor.api.domain.face.FaceId;
import com.facevisitor.api.domain.face.FaceImage;
import com.facevisitor.api.domain.face.FaceMeta;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.repo.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;

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