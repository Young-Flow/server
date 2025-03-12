package com.pitchain.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(requiredMode = Schema.RequiredMode.REQUIRED)
public record PtImgRes(
        int serialNum,
        String imgURL
) {
    public static PtImgRes createRes(int serialNum, String imgURL) {
        return new PtImgRes(serialNum, imgURL);
    }
}
