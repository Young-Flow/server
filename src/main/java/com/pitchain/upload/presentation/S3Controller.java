package com.pitchain.upload.presentation;

import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.upload.application.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @Operation(summary = "파일(이미지, 동영상) 저장 / 개발용")
    @PostMapping(value = "/s3", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart MultipartFile file,
                             @RequestParam S3UploadTarget s3UploadTarget) {
        String fileKey = s3Service.uploadFile(file, s3UploadTarget);
        return fileKey;
    }

    @Operation(summary = "파일(이미지) 삭제 / 개발용")
    @DeleteMapping("/s3/images")
    public void deleteImg(@RequestParam String fileKey) {
        s3Service.deleteImg(fileKey);
    }

}
