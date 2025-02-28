package com.pitchain.service;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.entity.*;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.MemberRepository;
import com.pitchain.repository.MySpRepository;
import com.pitchain.repository.SpRepository;
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
public class MySpServiceTest {

    @Autowired
    private MySpService mySpService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private SpRepository spRepository;
    @Autowired
    private MySpRepository mySpRepository;

    @Test
    void SP_좋아요_등록_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);
        Sp sp = saveSp(bm);

        //when
        mySpService.toggleLikeSp(sp.getId(), member.getId());

        //then
        boolean isLiked = mySpRepository.existsByMemberAndSp(member, sp);
        assertThat(isLiked).isTrue();
    }

    @Test
    void SP_좋아요_취소_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);
        Sp sp = saveSp(bm);

        MySp mySp = new MySp(member, sp);
        mySpRepository.save(mySp);

        //when
        mySpService.toggleLikeSp(sp.getId(), member.getId());

        //then
        boolean isLiked = mySpRepository.existsByMemberAndSp(member, sp);
        assertThat(isLiked).isFalse();
    }

    private Member saveMember() {
        return memberRepository.save(new Member(Country.USA, "profileImg"));
    }

    private Bm saveBm(Member member) {
        Bm bm = new Bm(member, "bmName", MainCategory.FOOD, "bmCompany", "logoImg",
                "bmIntro", "bmDescription", "bmDescriptionImg", "companyAddress", 100000L,
                1000L, 1000, LocalDate.now(), "longPitchUrl");
        return bmRepository.save(bm);
    }

    private Sp saveSp(Bm bm) {
        return spRepository.save(
                new Sp(bm, "spKey", "thumbnailImgKey", "spName"));
    }

}
