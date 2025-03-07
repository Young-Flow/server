package com.pitchain.service;

import com.pitchain.dto.res.MemberDetailRes;
import com.pitchain.entity.Member;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final EntityFacade entityFacade;
    private final S3Service s3Service;
    private final TokenUtil tokenUtil;

    public MemberDetailRes getMyDetail(Long memberId) {
        Member member = entityFacade.getMember(memberId);

        String profileImgURL = s3Service.getFileURL(member.getProfileImgKey());

        return MemberDetailRes.createRes(member, profileImgURL);
    }

    public String reissueAccessToken(String refreshToken) {
        return tokenUtil.reissueAccessToken(refreshToken);
    }
}
