package com.facevisitor.api.service.file;

import com.facevisitor.api.common.exception.PayloadTooLargeException;
import com.facevisitor.api.common.exception.UnsupportedMediaTypeException;
import com.facevisitor.api.common.utils.FileUtils;
import com.facevisitor.api.common.utils.StringUtils;
import com.facevisitor.api.service.aws.AWSS3Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@Transactional
public class FileService {

    public static final String UPLOADED_IMAGE_PATH = "images";

    public final static String FILE_PREFIX = "facevisitor";

    @Value("${aws.s3.url}")
    private String s3Url;

    @Autowired
    private AWSS3Service awss3Service;

    public String uploadImage(MultipartFile file) {
        String fileExt = FileUtils.extension(file.getContentType());

        if (!FileUtils.isImageExt(fileExt)) {
            throw new UnsupportedMediaTypeException(
                    "미디어 타입이 안맞습니다. (가능 확장자 \"gif\", \"jpeg\", \"jpg\", \"png\", \"svg\", \"blob\")");
        }

        if (file.getSize() >= FileUtils.convertByte(3, "MB")) {
            throw new PayloadTooLargeException("3MB 이상 업로드할 수 없습니다.");
        }

        String filename = this.getNewFilename(FileUtils.extension(file.getContentType()));
        String url = this.uploadToS3(UPLOADED_IMAGE_PATH, filename, file);
        return url;
    }


    public void deleteS3(String url) {
        if (StringUtils.isNotEmpty(url)) {
            String keyName = url.replaceAll(s3Url, "");
            awss3Service.deleteS3(keyName);
        }
    }


    private String uploadToS3(String path, String filename, MultipartFile file) {
        String keyName = path + "/" + filename;
        return awss3Service.uploadS3(file, keyName);
    }

    private String getNewFilename(String fileExtension) {
        return FILE_PREFIX + "_" + UUID.randomUUID().toString().replace("-", "") + fileExtension;
    }

}
