package com.pitchain.service;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.entity.Bm;
import com.pitchain.entity.BmScrap;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.MemberRepository;
import com.pitchain.repository.BmScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BmScrapService {

    private final BmScrapRepository bmScrapRepository;
    private final MemberRepository memberRepository;
    private final BmRepository bmRepository;

    @Transactional
    public void toggleScrapBm(Long bmId, MemberDetails memberDetails) {
        Member member = memberRepository.findById(memberDetails.id())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Bm bm = bmRepository.findById(bmId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BM_NOT_FOUND));

        if (isScraped(member, bm)) {
            cancelScrap(member, bm);
        } else {
            addScrap(member, bm);
        }
    }

    private boolean isScraped(Member member, Bm bm) {
        return bmScrapRepository.existsByMemberAndBm(member, bm);
    }

    private void addScrap(Member member, Bm bm) {
        BmScrap bmScrap = new BmScrap(member, bm);
        bmScrapRepository.save(bmScrap);
    }

    private void cancelScrap(Member member, Bm bm) {
        bmScrapRepository.deleteByMemberAndBm(member, bm);
    }


}
