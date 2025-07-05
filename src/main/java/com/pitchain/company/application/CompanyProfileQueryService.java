package com.pitchain.company.application;

import com.pitchain.company.domain.Company;
import com.pitchain.member.domain.Member;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.member.application.res.BaseMemberProfileRes;
import com.pitchain.company.application.res.CompanyProfileRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CompanyProfileQueryService {
    private final EntityFacade entityFacade;

    @Transactional(readOnly = true)
    public BaseMemberProfileRes getMyProfile(MemberDetails memberDetails) {
        Company company = entityFacade.getCompany(memberDetails);
        Member member = company.getMember();
        return CompanyProfileRes.createRes(member, company);
    }
}
