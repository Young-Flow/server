package com.pitchain.common.constant;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;

import java.util.Arrays;

public enum MainCategory {
    TECH_DIGITAL("테크/디지털"),
    COMMERCE_PLATFORM_COMMUNITY("커머스/플랫폼/커뮤니티"),
    FOOD("요식업"),
    SPORTS_OUTDOOR("스포츠/아웃도어"),
    ENTERTAINMENT("엔터테인먼트"),
    HR_LEGAL_BUSINESS("인사/법률/비지니스"),
    MANUFACTURING_HARDWARE("제조/하드웨어"),
    COMMUNICATION_SECURITY_DATA("통신/보안/데이터"),
    LIFESTYLE_LEISURE("라이프 스타일 및 여가"),
    SCIENCE_TECHNOLOGY("과학 전문기술"),
    CREATION_CULTURE("창작 및 문화"),
    TRANSPORTATION_MOBILITY("교통/모빌리티"),
    FINANCE_INSURANCE_FINTECH("금융/보험/핀테크");

    private final String koreanName;

    MainCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public static MainCategory from(String koreanName) {
        return Arrays.stream(values())
                .filter(val -> koreanName.equals(val.koreanName))
                .findFirst()
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.MAIN_CATEGORY_NOT_FOUND));
    }
}
