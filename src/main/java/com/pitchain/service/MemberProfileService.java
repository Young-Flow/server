package com.pitchain.service;

import com.pitchain.dto.req.BaseUpdateMemberReq;
import com.pitchain.dto.res.BaseMemberProfileRes;
import com.pitchain.jwt.MemberDetails;

public interface MemberProfileService {

    BaseMemberProfileRes getMyProfile(MemberDetails memberDetails);

    void updateMyProfile(MemberDetails memberDetails, BaseUpdateMemberReq req);
}
