package com.pitchain.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3UploadTarget {

    // COMPANY
    COMPANY_DESC(MIME.IMAGE), COMPANY_LOGO(MIME.IMAGE), COMPANY_PT(MIME.IMAGE), COMPANY_THUMBNAIL(MIME.IMAGE), COMPANY_VIDEO(MIME.VIDEO),

    // MEMBER
    MEMBER_PROFILE(MIME.IMAGE);

    private final MIME mime;

    public boolean isValidMimeType(String contentType) {
        return contentType != null && contentType.startsWith(this.mime.getType());
    }

    @Getter
    @RequiredArgsConstructor
    public enum MIME {
        IMAGE("image"),
        VIDEO("video");

        private final String type;
    }
}
