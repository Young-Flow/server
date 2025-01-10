package com.pitchain.dto;

import com.pitchain.entity.Sp;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SpWithLikeDto {
    Sp sp;
    boolean isLiked;
    Long likeCnt;
}
