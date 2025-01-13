package com.pitchain.service;


import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {
    private final S3Operations s3Operations;

    @Value("${spring.cloud.aws.s3.bucket.image}")
    private String imageBucket;
    @Value("${spring.cloud.aws.s3.bucket.video}")
    private String videoBucket;

    public String uploadFile(MultipartFile file, S3UploadTarget target) {
        String fileURL = Strings.EMPTY;

        try {
            validateMimeType(file, target);
            fileURL = upload(file, target);
        } catch (S3Exception e) {
            switch (e.statusCode()) {
                case 400 -> throw new GeneralHandler(ErrorStatus.BAD_REQUEST_FILE);
                case 401 -> throw new GeneralHandler(ErrorStatus.UNAUTHORIZED_S3);
                case 403 -> throw new GeneralHandler(ErrorStatus.FORBIDDEN_S3);
                case 500 -> throw new GeneralHandler(ErrorStatus.FAIL_FILE_UPLOAD);
                case 503 -> throw new GeneralHandler(ErrorStatus.UNAVAILABLE_S3);
            }
        } catch (IOException e) {
            throw new GeneralHandler(ErrorStatus.FAIL_STREAM_CONVERT);
        }
        return fileURL;
    }

    private static void validateMimeType(MultipartFile file, S3UploadTarget target) {
        if (!target.isValidMimeType(file.getContentType())) {
            throw new GeneralHandler(ErrorStatus.INVALID_MIME_TYPE);
        }
    }

    private String upload(MultipartFile file, S3UploadTarget target) throws IOException {
        String fileName = UUID.randomUUID().toString();
        String uploadFileUrl = putS3(file, fileName, target);

        return uploadFileUrl;
    }

    private String putS3(MultipartFile file, String fileName, S3UploadTarget target) throws IOException {
        String targetBucket = getTargetBucket(target);

        S3Resource uploadFile = s3Operations.upload(
                targetBucket,
                target.getPrefix() + fileName,
                file.getInputStream(),
                ObjectMetadata.builder()
                        .contentType(file.getContentType())
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .build()
        );

        return uploadFile.getURL().toString();
    }

    private String getTargetBucket(S3UploadTarget target) {
        return switch (target.getMime()) {
            case IMAGE -> imageBucket;
            case VIDEO -> videoBucket;
        };
    }

    public void deleteFile(String fileURL) {
        try {
            URI uri = URI.create(fileURL);
            String bucketName = extractBucketName(uri);
            String bucketKey = extractBucketKey(uri);
            s3Operations.deleteObject(bucketName, bucketKey);
        } catch (Exception e) {
            throw new GeneralHandler(ErrorStatus.INVALID_BUCKET_URL);
        }
    }

    public String extractBucketName(URI uri) {
        String host = uri.getHost();
        return host.split("\\.")[0];
    }

    public String extractBucketKey(URI uri) {
        return uri.getPath().substring(1);
    }
}
