package com.pitchain.service;

import com.pitchain.bm.domain.Bm;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.security.MemberDetails;
import com.pitchain.company.domain.Company;
import com.pitchain.member.domain.Member;
import com.pitchain.sp.domain.Sp;
import com.pitchain.splike.domain.SpLike;
import com.pitchain.splike.infrastucture.SpLikeRepository;
import com.pitchain.splike.application.SpLikeService;
import com.pitchain.util.EntitySaver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class SpLikeServiceTest {

    @Autowired
    private SpLikeService spLikeService;
    @Autowired
    private SpLikeRepository spLikeRepository;
    @Autowired
    private EntitySaver entitySaver;

    @Test
    void SP_좋아요_등록_성공() {
        //given
        Member individualMember = entitySaver.saveIndividualMember();

        MemberDetails individualMemberDetails = createIndividualMemberDetails(individualMember);

        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);
        Sp sp = entitySaver.saveSp(bm);

        //when
        spLikeService.toggleLikeSp(sp.getId(), individualMemberDetails);

        //then
        boolean isLiked = spLikeRepository.existsByMemberAndSp(individualMember, sp);
        assertThat(isLiked).isTrue();
    }

    @Test
    void SP_좋아요_취소_성공() {
        //given
        Member individualMember = entitySaver.saveIndividualMember();

        MemberDetails individualMemberDetails = createIndividualMemberDetails(individualMember);

        Member companyMember = entitySaver.saveCompanyMember();
        Company company = entitySaver.saveCompany(companyMember);
        Bm bm = entitySaver.saveBm(company);
        Sp sp = entitySaver.saveSp(bm);

        SpLike mySp = new SpLike(individualMember, sp);
        spLikeRepository.save(mySp);

        //when
        spLikeService.toggleLikeSp(sp.getId(), individualMemberDetails);

        //then
        boolean isLiked = spLikeRepository.existsByMemberAndSp(individualMember, sp);
        assertThat(isLiked).isFalse();
    }

    private MemberDetails createIndividualMemberDetails(Member member) {
        return new MemberDetails(member.getId(), MemberRole.INDIVIDUAL);
    }

}
