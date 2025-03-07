package com.pitchain.service;

import com.pitchain.common.constant.Country;
import com.pitchain.dto.res.MemberDetailRes;
import com.pitchain.entity.Member;
import com.pitchain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private S3Service s3Service;

    private Member saveMember() {
        return memberRepository.save(new Member(Country.USA, "profileImg.jpg"));
    }

    @Test
    void 내_정보_조회_성공() {
        //given
        Member member = saveMember();

        //when
        MemberDetailRes memberDetailRes = memberService.getMyDetail(member.getId());
        String profileImgURL = s3Service.getFileURL(member.getProfileImgKey());

        //then
        List<Member> members = memberRepository.findAll();
        assertThat(members.size()).isEqualTo(1);

        Member findMember = members.get(0);
        assertThat(memberDetailRes.profileImgURL()).isEqualTo(profileImgURL);
        assertThat(memberDetailRes.nickname()).isEqualTo(findMember.getNickname());
        assertThat(memberDetailRes.email()).isEqualTo(findMember.getEmail());
        assertThat(memberDetailRes.oauthProvider()).isEqualTo(findMember.getOauthProvider());

    }
}
