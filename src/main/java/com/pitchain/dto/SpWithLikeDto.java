package com.pitchain.dto;

import com.pitchain.entity.Sp;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class SpWithLikeDto {
    Sp sp;
    boolean isLiked;

    @QueryProjection
    public SpWithLikeDto(Sp sp, boolean isLiked) {
        this.sp = sp;
        this.isLiked = isLiked;
    }
}
