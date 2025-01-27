package com.pitchain.service;

import com.pitchain.common.constant.Country;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.entity.Bm;
import com.pitchain.entity.Member;
import com.pitchain.entity.MyBm;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.MemberRepository;
import com.pitchain.repository.MyBmRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class MyBmServiceTest {

    @Autowired
    private MyBmService myBmService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private MyBmRepository myBmRepository;

    @Test
    void BM_관심_등록_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        //when
        myBmService.toggleLikeBm(bm.getId(), member.getId());

        //then
        boolean isLiked = myBmRepository.existsByMemberAndBm(member, bm);
        assertThat(isLiked).isTrue();
    }

    @Test
    void BM_관심_취소_성공() {
        //given
        Member member = saveMember();
        Bm bm = saveBm(member);

        MyBm myBm = new MyBm(member, bm);
        myBmRepository.save(myBm);

        //when
        myBmService.toggleLikeBm(bm.getId(), member.getId());

        //then
        boolean isLiked = myBmRepository.existsByMemberAndBm(member, bm);
        assertThat(isLiked).isFalse();
    }

    private Member saveMember() {
        return memberRepository.save(new Member("name", UUID.randomUUID().toString(), Country.USA, "profileImg"));
    }

    private Bm saveBm(Member member) {
        Bm bm = new Bm(member, "bmName", MainCategory.FOOD,"bmCompany", "logoImg",
                "bmIntro", "bmDescription", "bmDescriptionImg", "companyAddress", 100000L,
                1000L, 1000, LocalDate.now(), "longPitchUrl");
        return bmRepository.save(bm);
    }

}