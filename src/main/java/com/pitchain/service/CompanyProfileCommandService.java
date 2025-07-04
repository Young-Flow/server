package com.pitchain.service;

import com.pitchain.dto.req.BaseMemberUpdateReq;
import com.pitchain.dto.req.CompanyUpdateReq;
import com.pitchain.entity.Company;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.EntityFacade;
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
