package com.facevisitor.api.service.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
public class AWSS3ServiceImpl implements AWSS3Service {

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.url}")
    private String url;

    @Autowired
    private AWSCredentialsService awsCredentialsService;

    private AmazonS3 client() {
        return AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsService.credentials()).withRegion(Regions.AP_NORTHEAST_2).build();
    }

    @Override
    public String uploadS3(MultipartFile file, String keyname) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        log.debug(String.format("bucketName : %s", bucketName));
        log.debug(String.format("keyname : %s", keyname));
        log.debug(String.format("content type : %s", metadata.getContentType()));
        log.debug(String.format("content length : %s", metadata.getContentLength()));
        try (InputStream inputStream = file.getInputStream()) {
            PutObjectResult pr = this.client().putObject(
                    new PutObjectRequest(bucketName, keyname, inputStream, metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
            );

//            return client().getUrl(bucketName,keyname).toString();
            return String.format("%s%s", url, keyname);

        } catch (AmazonServiceException ase) {

            log.error("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            log.error("Error Message:    " + ase.getMessage());
            log.error("HTTP SNSStatus Code: " + ase.getStatusCode());
            log.error("AWS Error Code:   " + ase.getErrorCode());
            log.error("Error Type:       " + ase.getErrorType());
            log.error("Request ID:       " + ase.getRequestId());

            throw ase;
        } catch (AmazonClientException ace) {
            log.error("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            log.error("Error Message: " + ace.getMessage());
            throw ace;
        } catch (IOException ie) {
            log.error("Error Message: " + ie.getMessage());
        }
        return null;
    }

    @Override
    public void deleteS3(String keyName) {

        try {

            log.debug("bucketName ::: {}", bucketName);
            log.debug("keyName ::: {}", keyName);
            this.client().deleteObject(new DeleteObjectRequest(bucketName, keyName));
            log.debug("delete success");

        } catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        } catch(SdkClientException e) {
            // Amazon S3 couldn't be contacted for a response, or the client
            // couldn't parse the response from Amazon S3.
            e.printStackTrace();
        }

    }

}
