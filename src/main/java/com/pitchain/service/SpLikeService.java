package com.pitchain.service;

import com.pitchain.jwt.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpLikeService {
    private final SpLikeCommandService spLikeCommandService;
    private final SpLikeQueryService spLikeQueryService;

    @Transactional
    public void toggleLikeSp(Long spId, MemberDetails memberDetails) {
        spLikeCommandService.toggleLikeSp(spId, memberDetails);
    }

    @Transactional(readOnly = true)
    public Long countBySpId(Long spId) {
        return spLikeQueryService.countBySpId(spId);
    }
}
