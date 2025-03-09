package com.pitchain.service;

import com.pitchain.dto.res.MemberDetailRes;
import com.pitchain.entity.Individual;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
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

    public MemberDetailRes getMyDetail(MemberDetails memberDetails) {
        Individual individual = entityFacade.getIndividual(memberDetails);

        Member member = individual.getMember();
        String profileImgURL = s3Service.getFileURL(member.getProfileImgKey());

        return MemberDetailRes.createRes(member, individual, profileImgURL);
    }

    public String reissueAccessToken(String refreshToken) {
        return tokenUtil.reissueAccessToken(refreshToken);
    }
}
