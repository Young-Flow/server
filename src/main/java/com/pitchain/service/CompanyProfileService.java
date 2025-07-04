package com.pitchain.service;

import com.pitchain.dto.req.BaseMemberUpdateReq;
import com.pitchain.dto.req.CompanyUpdateReq;
import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.jwt.MemberDetails;
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
