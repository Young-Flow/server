package com.pitchain.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;

@Getter
@RequiredArgsConstructor
public enum S3UploadTarget {

    // COMPANY
    COMPANY_DESC(MIME.IMAGE, "company/desc-img/"),
    COMPANY_PT(MIME.IMAGE, "company/pt-img/"),
    COMPANY_THUMBNAIL(MIME.IMAGE, "company/thumbnail-img/"),
    COMPANY_VIDEO(MIME.VIDEO, Strings.EMPTY),

    // MEMBER
    MEMBER_PROFILE(MIME.IMAGE, "member/profile-img/");

    private final MIME mime;
    private final String prefix;

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
