package com.pitchain.controller;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.apiPayload.annotation.ErrorApiResponse;
import com.pitchain.dto.req.BaseMemberUpdateReq;
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
    public BaseMemberProfileRes getMyProfile(@AuthenticationPrincipal MemberDetails memberDetails) {
        return memberService.getMyProfile(memberDetails);
    }

    @Operation(summary = "나의 프로필 수정", description = "memberRole 입력 필수")
    @PutMapping
    public void updateMyProfile(@AuthenticationPrincipal MemberDetails memberDetails,
                                @RequestBody @Valid BaseMemberUpdateReq req) {
        memberService.updateMyProfile(memberDetails, req);
    }

    @Operation(summary = "나의 프로필 이미지 수정")
    @PutMapping("/profileImgs")
    public void updateProfileImg(@AuthenticationPrincipal MemberDetails memberDetails, @RequestPart MultipartFile profileImg) {
        memberService.updateProfileImg(memberDetails, profileImg);
    }

    @Operation(summary = "회원 이메일 중복 여부 확인", description = "회사 생성 및 수정 시 사용, 중복이면 true 반환")
    @GetMapping("/emails")
    public boolean checkEmail(@RequestParam String email) {
        boolean isDuplicated = memberService.isDuplicatedEmail(email);
        return isDuplicated;
    }

    @Operation(summary = "Access/Refresh Token 재발급")
    @GetMapping("/tokens")
    public LoginRes reissueToken(@RequestParam String refreshToken) {
        LoginRes loginRes = memberService.reissueToken(refreshToken);
        return loginRes;
    }
}
