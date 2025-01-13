package com.pitchain.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Uploader {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket.img.company.desc}")
    private String companyDescBucket;
    @Value("${cloud.aws.s3.bucket.img.company.logo}")
    private String companyLogoBucket;
    @Value("${cloud.aws.s3.bucket.img.company.pt}")
    private String companyPtBucket;
    @Value("${cloud.aws.s3.bucket.img.company.thumbnail}")
    private String companyThumbnailBucket;

    @Value("${cloud.aws.s3.bucket.img.member.profile}")
    private String memberProfileBucket;

    @Value("${cloud.aws.s3.bucket.mp4}")
    private String mp4Bucket;

    /* MultipartFile을 전달받아 File로 전환 후 S3에 업로드 */
    public String uploadFile(MultipartFile file, S3UploadTarget target) {
        File uploadFile = null;
        String fileURL = null;

        try {
            validateFileType(file, target);

            Optional<File> optionalFile = convert(file);
            uploadFile = optionalFile.get();
            fileURL = upload(uploadFile, target);
        } catch (AmazonServiceException e) {
            switch (e.getStatusCode()) {
                case 400:
                    throw new GeneralHandler(ErrorStatus.BAD_REQUEST_FILE);
                case 401:
                    throw new GeneralHandler(ErrorStatus.UNAUTHORIZED_S3);
                case 403:
                    throw new GeneralHandler(ErrorStatus.FORBIDDEN_S3);
                case 500:
                    throw new GeneralHandler(ErrorStatus.FAIL_FILE_UPLOAD);
                case 503:
                    throw new GeneralHandler(ErrorStatus.UNAVAILABLE_S3);
            }
        } finally {
            if (uploadFile != null && uploadFile.exists())
                removeNewFile(uploadFile);
        }
        return fileURL;
    }

    private void validateFileType(MultipartFile file, S3UploadTarget target) {
        String contentType = file.getContentType();

        if (!target.isValidMimeType(contentType)) {
            throw new GeneralHandler(ErrorStatus.BAD_REQUEST_FILE);
        }
    }

    private String upload(File uploadFile, S3UploadTarget target) {
        String fileName = UUID.randomUUID().toString();
        String uploadFileUrl = putS3(uploadFile, fileName, target);

        return uploadFileUrl;
    }

    private String putS3(File uploadFile, String fileName, S3UploadTarget target) {
        String bucket = getBucket(target);

        amazonS3.putObject(
                new PutObjectRequest(bucket, fileName, uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private String getBucket(S3UploadTarget target) {
        return switch (target) {
            case COMPANY_DESC -> companyDescBucket;
            case COMPANY_LOGO -> companyLogoBucket;
            case COMPANY_PT -> companyPtBucket;
            case COMPANY_THUMBNAIL -> companyThumbnailBucket;
            case MEMBER_PROFILE -> memberProfileBucket;
            case COMPANY_VIDEO -> mp4Bucket;
        };
    }

    private void removeNewFile(File file) {
        if (file.delete()) {
            log.debug("파일이 삭제되었습니다.");
        } else {
            log.debug("파일 삭제에 실패했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (convertFile.createNewFile()) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(convertFile)) {
                    fileOutputStream.write(file.getBytes());
                }
                return Optional.of(convertFile);
            }
        } catch (IOException e) {
            throw new GeneralHandler(ErrorStatus.FAIL_FILE_CONVERT);
        }
        return Optional.empty();
    }

    public void deleteFile(String fileURL, S3UploadTarget target) {
        try {
            String key = getBucketKey(fileURL);
            String bucket = getBucket(target);
            amazonS3.deleteObject(bucket, key);
            log.debug("S3에서 파일이 삭제되었습니다. 파일명: " + key);
        } catch (AmazonServiceException e) {
            switch (e.getStatusCode()) {
                case 400:
                    throw new GeneralHandler(ErrorStatus.BAD_REQUEST_FILE);
                case 401:
                    throw new GeneralHandler(ErrorStatus.UNAUTHORIZED_S3);
                case 403:
                    throw new GeneralHandler(ErrorStatus.FORBIDDEN_S3);
                case 500:
                    throw new GeneralHandler(ErrorStatus.FAIL_FILE_DELETE);
                case 503:
                    throw new GeneralHandler(ErrorStatus.UNAVAILABLE_S3);
            }
        }
    }

    private String getBucketKey(String fileURL) {
        return fileURL.substring(fileURL.lastIndexOf("/") + 1);
    }
}
