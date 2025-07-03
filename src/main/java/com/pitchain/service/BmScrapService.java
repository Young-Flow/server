package com.pitchain.service;

import com.pitchain.jwt.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BmScrapService {

    private final BmScrapCommandService bmScrapCommandService;
    private final BmScrapQueryService bmScrapQueryService;

    public void toggleScrapBm(Long bmId, MemberDetails memberDetails) {
        bmScrapCommandService.toggleScrapBm(bmId, memberDetails);
    }

    public long countByBm(Long bmId) {
        return bmScrapQueryService.countByBm(bmId);
    }
}
