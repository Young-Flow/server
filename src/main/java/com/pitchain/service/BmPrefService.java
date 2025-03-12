package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BmPrefService {

    private final MemberRepository memberRepository;

    public void createCategoryPref(MemberDetails memberDetails, List<String> subCategoriesInKorean) {
        Member member = memberRepository.findById(memberDetails.id())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<SubCategory> subCategories = subCategoriesInKorean.stream()
                .map(SubCategory::from)
                .toList();

        member.addCategoryPref(subCategories);
    }
}
