package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.service.SpLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sps")
@RequiredArgsConstructor
public class SpLikeController {

    private final SpLikeService spLikeService;

    @PostMapping("/{spId}/like")
    public CustomApiResponse toggleLikeSp(@PathVariable("spId") Long spId,
                                          @AuthenticationPrincipal Long memberId) {
        spLikeService.toggleLikeSp(spId, memberId);
        return CustomApiResponse.onSuccess();
    }
}
