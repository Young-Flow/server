package com.pitchain.splike.application;

import com.pitchain.member.domain.Member;
import com.pitchain.sp.domain.Sp;
import com.pitchain.splike.domain.SpLike;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.splike.infrastucture.SpLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SpLikeCommandService {
    private final EntityFacade entityFacade;
    private final SpLikeRepository spLikeRepository;

    @Transactional
    public void toggleLikeSp(Long spId, MemberDetails memberDetails) {
        Member member = entityFacade.getMember(memberDetails.id());
        Sp sp = entityFacade.getSp(spId);

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
