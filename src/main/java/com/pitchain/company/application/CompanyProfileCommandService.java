package com.pitchain.company.application;

import com.pitchain.member.presentation.req.BaseMemberUpdateReq;
import com.pitchain.company.presentation.req.CompanyUpdateReq;
import com.pitchain.company.domain.Company;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.common.application.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CompanyProfileCommandService {
    private final EntityFacade entityFacade;

    @Transactional
    public void updateMyProfile(MemberDetails memberDetails, BaseMemberUpdateReq req) {
        Company company = entityFacade.getCompany(memberDetails);
        company.updateAddress(((CompanyUpdateReq) req).getAddress());
    }

}
