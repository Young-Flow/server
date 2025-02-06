package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.dto.res.MemberPreferenceInfoRes;
import com.pitchain.dto.res.PreferenceInfoRes;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.service.AIService;
import com.pitchain.service.SpService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AIController {

    private final AIService aiService;
    private final SpService spService;

    @Operation(summary = "AI 서버로 유저 선호도 정보 전송 / 개발용")
    @GetMapping("/dev-model")
    public String devAI() {
        List<MemberPreferenceInfoRes> memberPreferenceInfoResList = List.of(
                new MemberPreferenceInfoRes(1L, List.of(
                        new PreferenceInfoRes(1L, 10, true, false),
                        new PreferenceInfoRes(2L, 20, false, true)
                )),
                new MemberPreferenceInfoRes(2L, List.of(
                        new PreferenceInfoRes(3L, 30, true, true),
                        new PreferenceInfoRes(4L, 40, false, false)
                ))
        );

        new RestTemplate().postForObject("http://localhost:8000/models", memberPreferenceInfoResList, Void.class);
        return "ok";
    }

    @Operation(summary = "AI 테스트를 위한 유저 생성")
    @GetMapping("/dev-member")
    public Long createMember() {
        Long memberId = aiService.createMember();
        return memberId;
    }

    @Operation(summary = "AI 추천 SP 조회")
    @GetMapping("/dev-recommendation")
    public CustomApiResponse<List<SpDetailRes>> getRecommendation(@AuthenticationPrincipal Long memberId) {
        List<SpDetailRes> spDetailResList = spService.getRecommendationByPref(memberId);
        return CustomApiResponse.onSuccess(spDetailResList);
    }
}
