package com.pitchain.controller;

import com.pitchain.jwt.MemberDetails;
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
    public void toggleScrapBm(@PathVariable("bmId") Long bmId,
                              @AuthenticationPrincipal MemberDetails memberDetails) {
        bmScrapService.toggleScrapBm(bmId, memberDetails);
    }
}
