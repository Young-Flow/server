package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.req.BaseUpdateMemberReq;
import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberClaim;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final S3Service s3Service;
    private final TokenUtil tokenUtil;
    private final MemberRepository memberRepository;
    private final IndividualProfileService individualDetailService;
    private final CompanyProfileService companyDetailService;

    public BaseMemberProfileRes getMyProfile(MemberDetails memberDetails) {
        return switch (memberDetails.memberRole()) {
            case INDIVIDUAL -> individualDetailService.getMyProfile(memberDetails);
            case COMPANY -> companyDetailService.getMyProfile(memberDetails);
        };
    }

    public void updateMyProfile(MemberDetails memberDetails, BaseUpdateMemberReq req) {
        switch (memberDetails.memberRole()) {
            case INDIVIDUAL -> individualDetailService.updateMyProfile(memberDetails, req);
            case COMPANY -> companyDetailService.updateMyProfile(memberDetails, req);
        }
    }

    public void updateProfileImg(MemberDetails memberDetails, MultipartFile profileImg) {
        Member member = memberRepository.findById(memberDetails.id())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (member.hasProfileImg()) {
            s3Service.deleteImg(member.getProfileImgKey());
        }
        String logoImgKey = s3Service.uploadFile(profileImg, S3UploadTarget.MEMBER_PROFILE);

        member.updateProfileImgKey(logoImgKey);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    public LoginRes reissueToken(String refreshToken) {
        MemberClaim memberClaim = tokenUtil.getClaim(refreshToken);

        String newAccessToken = tokenUtil.issueAccessToken(memberClaim.getId(), memberClaim.getMemberRole());
        String newRefreshToken = tokenUtil.issueRefreshToken(memberClaim.getId(), memberClaim.getMemberRole());

        return new LoginRes(newAccessToken, newRefreshToken);
    }
}
