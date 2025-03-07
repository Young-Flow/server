package com.pitchain.controller;

import com.pitchain.common.apiPayload.annotation.ErrorApiResponse;
import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.dto.res.MemberDetailRes;
import com.pitchain.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "나의 정보 조회")
    @ErrorApiResponse(ErrorStatus.MEMBER_NOT_FOUND)
    @GetMapping
    public CustomApiResponse<MemberDetailRes> getMyDetail(@AuthenticationPrincipal Long memberId) {
        MemberDetailRes memberDetailRes = memberService.getMyDetail(memberId);
        return CustomApiResponse.onSuccess(memberDetailRes);
    }
}
