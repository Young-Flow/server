package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Exception;
import io.awspring.cloud.s3.S3Operations;
import io.awspring.cloud.s3.S3Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    private final S3Operations s3Operations;

    @Value("${spring.cloud.aws.s3.bucket.image}")
    private String imageBucket;
    @Value("${spring.cloud.aws.s3.bucket.video}")
    private String videoBucket;
    @Value("${spring.cloud.aws.s3.cdn}")
    private String cdnDomain;

    public String uploadFile(MultipartFile file, S3UploadTarget target) {
        String fileKey = Strings.EMPTY;
        if (file == null || file.isEmpty())
            return fileKey;

        try {
            validateMimeType(file, target);
            fileKey = upload(file, target);
        } catch (IOException e) {
            throw new GeneralHandler(ErrorStatus.FAIL_STREAM_CONVERT);
        } catch (S3Exception e) {
            throw new GeneralHandler(ErrorStatus.FAIL_S3_UPLOAD);
        }
        return fileKey;
    }

    public String getFileURL(String fileKey) {
        if (fileKey == null) {
            return Strings.EMPTY;
        }
        return cdnDomain + "/" + fileKey;
    }

    private static void validateMimeType(MultipartFile file, S3UploadTarget target) {
        if (!target.isValidMimeType(file.getContentType())) {
            throw new GeneralHandler(ErrorStatus.INVALID_MIME_TYPE);
        }
    }

    private String upload(MultipartFile file, S3UploadTarget target) throws IOException {
        String fileName = UUID.randomUUID() + file.getOriginalFilename();
        String fileKey = putS3(file, fileName, target);

        return fileKey;
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

        return uploadFile.getFilename();  //key
    }

    public void deleteImg(String fileKey) {
        deleteFile(imageBucket, fileKey);
    }

    public void deleteVid(String fileKey) {
        deleteFile(videoBucket, fileKey);
    }

    private void deleteFile(String bucket, String fileKey) {
        try {
            s3Operations.deleteObject(bucket, fileKey);
        } catch (Exception e) {
            throw new GeneralHandler(ErrorStatus.INVALID_BUCKET_URL);
        }
    }

    private String getTargetBucket(S3UploadTarget target) {
        return switch (target.getMime()) {
            case IMAGE -> imageBucket;
            case VIDEO -> videoBucket;
        };
    }
}
