package com.pitchain.bm.infrastucture.dto;

import com.pitchain.bm.domain.Bm;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BmWithScrapDto {
    Bm bm;
    boolean isScraped;

    @QueryProjection
    public BmWithScrapDto(Bm bm, boolean isScraped) {
        this.bm = bm;
        this.isScraped = isScraped;
    }
}
