package com.facevisitor.api.service.aws;

import org.springframework.web.multipart.MultipartFile;

public interface AWSS3Service {

    /**
     * 이미지 업로드
     *
     * @param file        멀티 파트
     * @param dirName     S3 경로
     * @return Keyname (full path)
     */
    String uploadS3(MultipartFile file, String dirName);

    void deleteS3(String keyName);

}
