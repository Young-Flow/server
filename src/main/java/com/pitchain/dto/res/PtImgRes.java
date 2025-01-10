package com.pitchain.dto.res;

import com.pitchain.entity.PtImg;

public record PtImgRes(
        int serialNum,
        String img
) {
    public static PtImgRes createRes(PtImg ptImg) {
        return new PtImgRes(ptImg.getSerialNum(), ptImg.getImg());
    }
}
