package com.pitchain.member.infrastucture;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.company.application.CompanyProfileService;
import com.pitchain.individual.application.IndividualProfileService;
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
            case MEMBER -> throw new GeneralException(ErrorStatus._BAD_REQUEST);
        };
    }

}
