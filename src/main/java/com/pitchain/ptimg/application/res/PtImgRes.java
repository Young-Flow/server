package com.pitchain.ptimg.application.res;

import com.pitchain.common.annotation.S3Url;
import jakarta.validation.constraints.NotEmpty;

public record PtImgRes(
        int serialNum,
        @NotEmpty
        @S3Url
        String imgURL
) {
    public static PtImgRes createRes(int serialNum, String imgKey) {
        return new PtImgRes(serialNum, imgKey);
    }
}
