package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.constant.SubCategory;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.entity.CategoryPref;
import com.pitchain.entity.Member;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        Member member = saveIndividual();
        MemberDetails memberDetails = createIndividualMemberDetails(member);
        List<String> subCategories = List.of(SubCategory.BEVERAGE_COFFEE.getKoreanName(), SubCategory.ALCOHOL.getKoreanName());

        //when
        bmPrefService.createCategoryPref(memberDetails, subCategories);

        //then
        List<CategoryPref> categoryPrefs = member.getCategoryPrefs();
        for (int i = 0; i < subCategories.size(); i++) {
            SubCategory savedSubCategory = categoryPrefs.get(i).getSubCategory();
            assertThat(savedSubCategory.getKoreanName()).isEqualTo(subCategories.get(i));
        }

    }

    @Test
    void 카테고리_선호_등록_실패() {
        //given
        Member member = saveIndividual();
        MemberDetails memberDetails = createIndividualMemberDetails(member);
        String randomSubCategoryInKorean = "생맥주";
        List<String> subCategories = List.of(SubCategory.BEVERAGE_COFFEE.getKoreanName(), randomSubCategoryInKorean);

        //when
        GeneralHandler error = assertThrows(GeneralHandler.class, () -> bmPrefService.createCategoryPref(memberDetails, subCategories));

        //then
        assertThat(error.getErrorStatus()).isEqualTo(ErrorStatus.SUB_CATEGORY_NOT_FOUND);
    }

    private Member saveIndividual() {
        return memberRepository.save(Member.createIndividualMember("email", "name"));
    }

    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }

}
