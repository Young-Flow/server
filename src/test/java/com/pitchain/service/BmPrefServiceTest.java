package com.pitchain.service;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.entity.CategoryPref;
import com.pitchain.entity.Member;
import com.pitchain.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class BmPrefServiceTest {

    @Autowired
    BmPrefService bmPrefService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    void 카테고리_선호_등록_성공() {
        //given
        Member member = saveMember();
        List<SubCategory> subCategories = List.of(SubCategory.BEVERAGE_COFFEE, SubCategory.ALCOHOL);

        //when
        bmPrefService.createCategoryPref(member.getId(), subCategories);

        //then
        List<CategoryPref> categoryPrefs = member.getCategoryPrefs();
        for (int i = 0; i < subCategories.size(); i++) {
            SubCategory savedSubCategory = categoryPrefs.get(i).getSubCategory();
            Assertions.assertThat(savedSubCategory).isEqualTo(subCategories.get(i));
        }

    }

    private Member saveMember() {
        return memberRepository.save(new Member("name", UUID.randomUUID().toString(), Country.USA, "profileImg.jpg"));
    }

}