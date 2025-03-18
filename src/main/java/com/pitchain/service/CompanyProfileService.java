package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.req.BaseUpdateMemberReq;
import com.pitchain.dto.req.UpdateCompanyReq;
import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.dto.res.CompanyProfileRes;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyProfileService implements MemberProfileService {

    private final CompanyRepository companyRepository;
    private final S3Service s3Service;

    @Override
    public BaseMemberProfileRes getMyProfile(MemberDetails memberDetails) {
        Company company = getCompany(memberDetails);
        Member member = company.getMember();
        String profileImgURL = s3Service.getFileURL(member.getProfileImgKey());
        return CompanyProfileRes.createRes(member, company, profileImgURL);
    }

    @Override
    @Transactional
    public void updateMyProfile(MemberDetails memberDetails, BaseUpdateMemberReq req) {
        Company company = getCompany(memberDetails);
        company.updateAddress(((UpdateCompanyReq) req).getAddress());
    }

    private Company getCompany(MemberDetails memberDetails) {
        if (memberDetails.memberRole().equals(MemberRole.INDIVIDUAL)) {
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
        }

        return companyRepository.findByMemberId(memberDetails.id())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.COMPANY_NOT_FOUND));
    }
}
