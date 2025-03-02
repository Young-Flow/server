package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.service.BmScrapService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bms")
@RequiredArgsConstructor
public class BmScrapController {

    private final BmScrapService bmScrapService;

    @PostMapping("/{bmId}/scrap")
    public CustomApiResponse toggleScrapBm(@PathVariable("bmId") Long bmId,
                                           @AuthenticationPrincipal Long memberId) {
        bmScrapService.toggleScrapBm(bmId, memberId);
        return CustomApiResponse.onSuccess();
    }
}
