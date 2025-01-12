package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.service.SpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/sps")
@RestController
public class SpController {
    private final SpService spService;

    @GetMapping
    public CustomApiResponse<List<SpDetailRes>> getSpDetails(@AuthenticationPrincipal Long memberId) {
        List<SpDetailRes> spDetailResList = spService.getSpDetails(memberId);
        return CustomApiResponse.onSuccess(spDetailResList);
    }

    @GetMapping("/{spId}")
    public CustomApiResponse<SpDetailRes> getSpDetail(@AuthenticationPrincipal Long memberId,
                                                      @PathVariable Long spId) {
        SpDetailRes spDetailRes = spService.getSpDetail(memberId, spId);
        return CustomApiResponse.onSuccess(spDetailRes);
    }

}
