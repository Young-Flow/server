package com.pitchain.dto.res;

import jakarta.validation.constraints.NotEmpty;

public record PtImgRes(
        int serialNum,
        @NotEmpty
        String imgURL
) {
    public static PtImgRes createRes(int serialNum, String imgURL) {
        return new PtImgRes(serialNum, imgURL);
    }
}
