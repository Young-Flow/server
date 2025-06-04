package com.pitchain.service;

import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.entity.*;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
public class SpLikeServiceTest {

    @Autowired
    private SpLikeService spLikeService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private SpRepository spRepository;
    @Autowired
    private SpLikeRepository spLikeRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void SP_좋아요_등록_성공() {
        //given
        Member individual = saveIndividual();
        MemberDetails individualMemberDetails = createIndividualMemberDetails(individual);

        Company company = saveCompany();
        Bm bm = saveBm(company);
        Sp sp = saveSp(bm);

        //when
        spLikeService.toggleLikeSp(sp.getId(), individualMemberDetails);

        //then
        boolean isLiked = spLikeRepository.existsByMemberAndSp(individual, sp);
        assertThat(isLiked).isTrue();
    }

    @Test
    void SP_좋아요_취소_성공() {
        //given
        Member individual = saveIndividual();
        MemberDetails individualMemberDetails = createIndividualMemberDetails(individual);

        Company company = saveCompany();
        Bm bm = saveBm(company);
        Sp sp = saveSp(bm);

        SpLike mySp = new SpLike(individual, sp);
        spLikeRepository.save(mySp);

        //when
        spLikeService.toggleLikeSp(sp.getId(), individualMemberDetails);

        //then
        boolean isLiked = spLikeRepository.existsByMemberAndSp(individual, sp);
        assertThat(isLiked).isFalse();
    }

    private Member saveIndividual() {
        return memberRepository.save(Member.createIndividualMember("email", "name"));
    }

    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }

    private Company saveCompany() {
        Member member = memberRepository.save(Member.createCompanyMember("email"));
        return companyRepository.save(new Company(member, "encodedPassword"));
    }


    private Bm saveBm(Company company) {
        return bmRepository.save(Bm.create(company, "bmName", MainCategory.FOOD, "bmIntro", "bmDescription",
                "bmDescriptionImg", "companyAddress", 100000L, 1000L, 1000, LocalDate.now(), "longPitchUrl"));
    }

    private Sp saveSp(Bm bm) {
        return spRepository.save(
                new Sp(bm, "spKey", "thumbnailImgKey", "spName"));

    }
}
