package com.pitchain.dto.res;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pitchain.common.converter.S3UrlSerializer;
import jakarta.validation.constraints.NotEmpty;

public record PtImgRes(
        int serialNum,
        @NotEmpty
        @JsonSerialize(using = S3UrlSerializer.class)
        String imgURL
) {
    public static PtImgRes createRes(int serialNum, String imgURL) {
        return new PtImgRes(serialNum, imgURL);
    }
}
