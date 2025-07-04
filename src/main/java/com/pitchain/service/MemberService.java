package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.dto.req.BaseMemberUpdateReq;
import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberClaims;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final S3Service s3Service;
    private final TokenUtil tokenUtil;
    private final MemberRepository memberRepository;
    private final MemberProfileServiceFactory memberProfileServiceFactory;

    @Transactional(readOnly = true)
    public BaseMemberProfileRes getMyProfile(MemberDetails memberDetails) {
        MemberProfileService memberProfileService = memberProfileServiceFactory.getMemberProfileService(memberDetails);
        return memberProfileService.getMyProfile(memberDetails);
    }

    @Transactional
    public void updateMyProfile(MemberDetails memberDetails, BaseMemberUpdateReq req) {
        MemberProfileService memberProfileService = memberProfileServiceFactory.getMemberProfileService(memberDetails);
        memberProfileService.updateMyProfile(memberDetails, req);
    }

    @Transactional
    public void updateProfileImg(MemberDetails memberDetails, MultipartFile profileImg) {
        Member member = memberRepository.findById(memberDetails.id())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (member.hasProfileImg()) {
            s3Service.deleteImg(member.getProfileImgKey());
        }
        String logoImgKey = s3Service.uploadFile(profileImg, S3UploadTarget.MEMBER_PROFILE);

        member.updateProfileImgKey(logoImgKey);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatedEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public LoginRes reissueToken(String refreshToken) {
        MemberClaims memberClaims = tokenUtil.getClaim(refreshToken);

        String newAccessToken = tokenUtil.issueAccessToken(memberClaims.getId(), memberClaims.getMemberRole());
        String newRefreshToken = tokenUtil.issueRefreshToken(memberClaims.getId(), memberClaims.getMemberRole());

        return new LoginRes(newAccessToken, newRefreshToken);
    }

    @Transactional(readOnly = true)
    public void validateEmailConflict(String email) {
        if (memberRepository.existsByEmail(email))
            throw new GeneralException(ErrorStatus.MEMBER_EMAIL_CONFLICT);
    }

    @Transactional
    public Member saveCompanyMember(String email) {
        Member member = Member.createCompanyMember(email);
        return memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Transactional
    public Member saveIndividualMember(String email, String nickname) {
        Member member = Member.createIndividualMember(email, nickname);
        return memberRepository.save(member);
    }
}
