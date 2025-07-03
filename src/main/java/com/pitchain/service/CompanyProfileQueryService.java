package com.pitchain.service;

import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.dto.res.CompanyProfileRes;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.EntityFacade;
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
