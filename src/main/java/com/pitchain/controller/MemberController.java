package com.pitchain.controller;

import com.pitchain.common.apiPayload.annotation.ErrorApiResponse;
import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.dto.req.BaseUpdateMemberReq;
import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "나의 프로필 조회")
    @ErrorApiResponse(ErrorStatus.MEMBER_NOT_FOUND)
    @GetMapping
    public CustomApiResponse<BaseMemberProfileRes> getMyProfile(@AuthenticationPrincipal MemberDetails memberDetails) {
        return CustomApiResponse.onSuccess(memberService.getMyProfile(memberDetails));
    }

    @Operation(summary = "나의 프로필 수정", description = "memberRole 입력 필수")
    @PutMapping
    public CustomApiResponse<Void> updateMyProfile(@AuthenticationPrincipal MemberDetails memberDetails,
                                                   @RequestBody @Valid BaseUpdateMemberReq req) {
        memberService.updateMyProfile(memberDetails, req);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "나의 프로필 이미지 수정")
    @PutMapping("/profileImgs")
    public CustomApiResponse<Void> updateProfileImg(@AuthenticationPrincipal MemberDetails memberDetails, @RequestPart MultipartFile profileImg) {
        memberService.updateProfileImg(memberDetails, profileImg);
        return CustomApiResponse.onSuccess();
    }

    @Operation(summary = "회원 이메일 중복 여부 확인", description = "회사 생성 및 수정 시 사용, 중복이면 true 반환")
    @GetMapping("/emails")
    public CustomApiResponse<Boolean> checkEmail(@RequestParam String email) {
        boolean isDuplicated = memberService.isDuplicatedEmail(email);
        return CustomApiResponse.onSuccess(isDuplicated);
    }

    @Operation(summary = "Access/Refresh Token 재발급")
    @GetMapping("/tokens")
    public CustomApiResponse<LoginRes> reissueToken(@RequestParam String refreshToken) {
        LoginRes loginRes = memberService.reissueToken(refreshToken);
        return CustomApiResponse.onSuccess(loginRes);
    }
}
