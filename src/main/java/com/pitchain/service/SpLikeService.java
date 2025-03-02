package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.entity.Member;
import com.pitchain.entity.Sp;
import com.pitchain.entity.SpLike;
import com.pitchain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SpLikeService {

    private final SpLikeRepository spLikeRepository;
    private final MemberRepository memberRepository;
    private final SpRepository spRepository;

    @Transactional
    public void toggleLikeSp(Long spId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Sp sp = spRepository.findById(spId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.SP_NOT_FOUND));

        if (isLiked(member, sp)) {
            cancelLike(member, sp);
        } else {
            addLike(member, sp);
        }
    }

    private boolean isLiked(Member member, Sp sp) {
        return spLikeRepository.existsByMemberAndSp(member, sp);
    }

    private void addLike(Member member, Sp sp) {
        SpLike mySp = new SpLike(member, sp);
        spLikeRepository.save(mySp);
    }

    private void cancelLike(Member member, Sp sp) {
        spLikeRepository.deleteByMemberAndSp(member, sp);
    }

}
