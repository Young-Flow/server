package com.pitchain.company.application;

import com.pitchain.member.infrastucture.MemberProfileService;
import com.pitchain.member.presentation.req.BaseMemberUpdateReq;
import com.pitchain.company.presentation.req.CompanyUpdateReq;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.member.application.res.BaseMemberProfileRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyProfileService implements MemberProfileService {
    private final CompanyProfileCommandService companyProfileCommandService;
    private final CompanyProfileQueryService companyProfileQueryService;

    @Override
    public BaseMemberProfileRes getMyProfile(MemberDetails memberDetails) {
        return companyProfileQueryService.getMyProfile(memberDetails);
    }

    @Override
    @Transactional
    public void updateMyProfile(MemberDetails memberDetails, BaseMemberUpdateReq req) {
        companyProfileCommandService.updateMyProfile(memberDetails, (CompanyUpdateReq) req);
    }
}
