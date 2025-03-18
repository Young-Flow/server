package com.pitchain.service;

import com.pitchain.jwt.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceFactory {

    private final IndividualProfileService individualProfileService;
    private final CompanyProfileService companyProfileService;

    public MemberProfileService getMemberProfileService(MemberDetails memberDetails) {
        return switch (memberDetails.memberRole()) {
            case INDIVIDUAL -> individualProfileService;
            case COMPANY -> companyProfileService;
        };
    }

}
