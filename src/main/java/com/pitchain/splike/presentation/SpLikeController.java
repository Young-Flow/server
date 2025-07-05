package com.pitchain.splike.presentation;

import com.pitchain.common.security.MemberDetails;
import com.pitchain.splike.application.SpLikeService;
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
    public void toggleLikeSp(@PathVariable("spId") Long spId,
                             @AuthenticationPrincipal MemberDetails memberDetails) {
        spLikeService.toggleLikeSp(spId, memberDetails);
    }
}
