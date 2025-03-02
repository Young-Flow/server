package com.pitchain.dto;

import com.pitchain.entity.Bm;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BmWithScrapDto {
    Bm bm;
    boolean isScraped;
}
