package com.yurakim.readingtrace.shared.service.serviceImpl;

import com.yurakim.readingtrace.shared.constant.UploadType;
import com.yurakim.readingtrace.shared.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.utils.Validate;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;
    @Value("${aws.s3.region}")
    private String region;

    public boolean checkBucketExistence(){
        try{
            Validate.notEmpty(bucketName, "The bucket name must not be null or an empty string");
            s3Client.getBucketAcl(request -> request.bucket(bucketName));
            return true;
        }catch(AwsServiceException exception){
            if ((exception.statusCode() == HttpStatusCode.MOVED_PERMANENTLY) || "AccessDenied".equals(exception.awsErrorDetails().errorCode())) {
                return true;
            }
            if (exception.statusCode() == HttpStatusCode.NOT_FOUND) {
                return false;
            }
            throw exception;
        }
    }

    //TODO: clean up logic in uploadFile and moveTempToPermanent methods
    public String uploadFile(Long userId, MultipartFile file, UploadType uploadType) {

        if(!checkBucketExistence()) return null;

        String baseDir = "user/profile/";
        String subDir = (uploadType == UploadType.TEMP) ? "tmp/" : "final/";

        String fileKey = userId + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        String fullKey = baseDir + subDir + fileKey;

        try {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fullKey)
                    .build(),
                    RequestBody.fromBytes(file.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fullKey;
    }

    public String moveTempToPermanent(Long userId, String tempUrl){
        String tempKey = "user/profile/" + tempUrl.substring(tempUrl.indexOf("tmp/"));
        String fileName = tempKey.substring(tempKey.lastIndexOf("_") + 1);
        String permanentKey = "user/profile/final/" + userId + "_" + fileName;

        System.out.println("TEMP KEY: " + tempKey);


        CopyObjectRequest copyObjectRequest = CopyObjectRequest.builder()
                .sourceBucket(bucketName)
                .sourceKey(tempKey)
                .destinationBucket(bucketName)
                .destinationKey(permanentKey)
                .build();
        s3Client.copyObject(copyObjectRequest);

        String tempPrefix = "user/profile/tmp/" + userId;
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(tempPrefix)
                .build();
        s3Client.listObjectsV2(listObjectsV2Request).contents().forEach(object-> {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(object.key())
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        });

        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + permanentKey;
    }

}
