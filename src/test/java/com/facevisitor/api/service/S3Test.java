package com.facevisitor.api.service;

import com.facevisitor.api.service.file.FileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class S3Test {

    @Autowired
    FileService fileService;

    @Test
    public void deleteS3() throws Exception{
        fileService.deleteS3("https://facevisitor-bucket.s3.ap-northeast-2.amazonaws.com/images/facevisitor_1b706276296542d68aa049dc503c3c61.png");
    }



}
