package com.pitchain.util;

import com.pitchain.bm.domain.Bm;
import com.pitchain.bm.infrastucture.BmRepository;
import com.pitchain.bmscrap.infrastucture.BmScrapRepository;
import com.pitchain.bmsubcategory.infrastructure.BmSubCategoryRepository;
import com.pitchain.common.constant.MainCategory;
import com.pitchain.common.constant.OauthProvider;
import com.pitchain.investment.infrastucture.InvestmentRepository;
import com.pitchain.company.domain.Company;
import com.pitchain.individual.domain.Individual;
import com.pitchain.member.domain.Member;
import com.pitchain.company.infrastructure.CompanyRepository;
import com.pitchain.individual.infrastructure.IndividualRepository;
import com.pitchain.member.infrastucture.MemberRepository;
import com.pitchain.mybmhistory.infrastructure.MyBmHistoryRepository;
import com.pitchain.mysphistory.infrastucture.MySpHistoryRepository;
import com.pitchain.ptimg.infrastructure.PtImgRepository;
import com.pitchain.sp.domain.Sp;
import com.pitchain.sp.infrastucture.SpRepository;
import com.pitchain.splike.infrastucture.SpLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class EntitySaver {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private IndividualRepository individualRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private BmRepository bmRepository;
    @Autowired
    private BmScrapRepository bmScrapRepository;
    @Autowired
    private BmSubCategoryRepository bmSubCategoryRepository;
    @Autowired
    private PtImgRepository ptImgRepository;
    @Autowired
    private InvestmentRepository investmentRepository;
    @Autowired
    private SpRepository spRepository;
    @Autowired
    private SpLikeRepository spLikeRepository;
    @Autowired
    private MyBmHistoryRepository myBmHistoryRepository;
    @Autowired
    private MySpHistoryRepository mySpHistoryRepository;

    public Member saveIndividualMember() {
        return memberRepository.save(Member.createIndividualMember(UUID.randomUUID().toString(), "encodedPassword"));
    }

    public Member saveCompanyMember() {
        return memberRepository.save(Member.createCompanyMember("testCompany"));
    }

    public Individual saveIndividual(Member member) {
        return individualRepository.save(Individual.of(member, UUID.randomUUID().toString(), OauthProvider.KAKAO));

    }

    public Company saveCompany(Member member) {
        return companyRepository.save(Company.createUnverifiedCompany(member, "encodedPassword"));
    }

    public Bm saveBm(Company company) {
        return bmRepository.save(Bm.create(company, "name", MainCategory.FOOD, "intro", "description", "desc_img_key",
                "address", 100000L, 1000L, 1000, LocalDate.now(), "long_pitch_url"
        ));
    }

    public Sp saveSp(Bm bm) {
        return spRepository.save(Sp.of(bm, "thumbnailImgKey", "name"));
    }
}
