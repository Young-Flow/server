package com.pitchain.dto.res;

public record PtImgRes(
        int serialNum,
        String imgURL
) {
    public static PtImgRes createRes(int serialNum, String imgURL) {
        return new PtImgRes(serialNum, imgURL);
    }
}
