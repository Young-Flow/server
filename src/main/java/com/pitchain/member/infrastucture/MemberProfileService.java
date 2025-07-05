package com.pitchain.member.infrastucture;

import com.pitchain.member.presentation.req.BaseMemberUpdateReq;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.member.application.res.BaseMemberProfileRes;

public interface MemberProfileService {

    BaseMemberProfileRes getMyProfile(MemberDetails memberDetails);

    void updateMyProfile(MemberDetails memberDetails, BaseMemberUpdateReq req);
}
