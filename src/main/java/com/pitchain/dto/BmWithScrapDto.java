package com.pitchain.dto;

import com.pitchain.entity.Bm;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
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
