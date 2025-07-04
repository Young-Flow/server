package com.pitchain.service;

import com.pitchain.dto.req.BaseMemberUpdateReq;
import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.jwt.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndividualProfileService implements MemberProfileService {
    private final IndividualProfileCommandService individualProfileCommandService;
    private final IndividualProfileQueryService individualProfileQueryService;

    @Transactional(readOnly = true)
    @Override
    public BaseMemberProfileRes getMyProfile(MemberDetails memberDetails) {
        return individualProfileQueryService.getMyProfile(memberDetails);
    }

    @Transactional
    @Override
    public void updateMyProfile(MemberDetails memberDetails, BaseMemberUpdateReq req) {
        individualProfileCommandService.updateMyProfile(memberDetails, req);
    }
}
