package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.entity.Member;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryPrefService {

    private final MemberRepository memberRepository;

    public void createCategoryPref(Long memberId, List<SubCategory> subCategories) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.addCategoryPref(subCategories);
    }
}
