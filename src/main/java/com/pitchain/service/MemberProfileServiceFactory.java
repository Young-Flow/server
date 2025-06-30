package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
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
            case MEMBER -> throw new GeneralException(ErrorStatus._BAD_REQUEST);
        };
    }

}
