package com.yurakim.readingtrace.shared.service;

import com.yurakim.readingtrace.shared.constant.UploadType;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

    boolean checkBucketExistence();

    String uploadFile(Long userId, MultipartFile file, UploadType uploadType);
}
