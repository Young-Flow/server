package com.pitchain.common.constant;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum SubCategory {
    // TECH_DIGITAL
    AI_ML("AI 및 머신러닝"),
    SAAS("SaaS"),
    METAVERSE_CONTENT("메타버스 콘텐츠"),
    APP_DEVELOPMENT("앱 개발 관련 콘텐츠"),
    BLOCKCHAIN_WEB3("블록체인 및 Web3"),
    IT_HARDWARE_DEVICES("IT 하드웨어 및 디바이스"),

    // COMMERCE_PLATFORM_COMMUNITY
    ECOMMERCE("전자상거래"),
    RESALE("중고거래 및 리셀"),
    LOCAL_COMMUNITY_NETWORK("지역커뮤니티 및 네트워크"),
    REVIEW_CURATION("리뷰 및 큐레이션 기반"),

    // FOOD
    LOCAL_FOOD("로컬푸드"),
    FOREIGN_FOOD("해외음식"),
    READY_MEAL_KIT("간편식/밀키트"),
    HEALTH_DIET("헬스케어/다이어트"),
    DESSERT_SNACKS("디저트/간식"),
    SAUCES_SEASONINGS("소스/조미료"),
    ALCOHOL("주류"),
    BEVERAGE_COFFEE("음료/커피"),

    // SPORTS_OUTDOOR
    ESPORTS_PRODUCTS("e-스포츠 관련 제품"),
    CAMPING("캠핑"),
    HOME_TRAINING("홈트레이닝"),
    HIKING("등산"),
    GOLF("골프"),
    RUNNING("러닝"),
    FISHING("낚시"),
    TENNIS("테니스"),
    CYCLING("자전거"),
    SPORTS_LESSON_TRAINING("스포츠 레슨/트레이닝"),

    // ENTERTAINMENT
    ENTERTAINMENT_CONTENT("오락 콘텐츠"),
    BOARD_GAMES_TPRG("보드게임 및 TPRG"),
    ONLINE_GAMES("온라인 게임"),
    DIGITAL_GAMES("디지털 게임"),
    WEBTOONS_STORY_CONTENT("웹툰 및 스토리 콘텐츠"),
    LIVE_PERFORMANCE("대중 공연"),
    MUSIC("음악"),
    VIDEO("영상"),

    // HR_LEGAL_BUSINESS
    HR_SOLUTION("HR 솔루션"),
    LABOR_MANAGEMENT("노무 관리"),
    LEGAL_TECH("리걸 테크"),
    CONSULTING("컨설팅"),
    REMOTE_WORK("리모트 워크"),
    PR_MARKETING("홍보&마케팅"),

    // MANUFACTURING_HARDWARE
    HIGH_TECH("고기술(반도체, 항공우주)"),
    MEDIUM_TECH("중고기술(자동차, 산업 기계)"),
    LOW_TECH("중저기술(일반 기계, 소비재)"),
    TRADITIONAL_CRAFT("저기술(전통 공예품, 소규모 생산품)"),

    // COMMUNICATION_SECURITY_DATA
    NETWORK_COMMUNICATION("네트워크 및 통신 기술"),
    DATA_ANALYTICS("데이터 분석 및 처리"),
    CYBER_SECURITY("정보 보안 및 사이버 보안"),

    // LIFESTYLE_LEISURE
    LEISURE_TOURISM("레저 및 관광"),
    BEAUTY_WELLNESS("뷰티 및 웰니스"),
    HOME_LIVING("홈&리빙(가구, 인테리어, 주방용품)"),
    FASHION_CLOTHING("패션 및 의류"),
    PETS("반려동물"),
    LODGING_RENTAL("숙박 및 임대업"),

    // SCIENCE_TECHNOLOGY
    LIFE_SCIENCE_BIOTECH("생명과학 및 바이오테크"),
    ENERGY_ENVIRONMENT_TECH("에너지 및 환경 기술"),
    RND_LAB_SERVICES("R&D 및 실험 서비스"),
    ESG("ESG"),

    // CREATION_CULTURE
    INDUSTRIAL_DESIGN("산업디자인"),
    ART_CRAFT("미술 및 공예"),
    PUBLISHING_LITERATURE("출판 및 문학"),
    PERFORMING_ARTS_EXHIBITION("공연 예술 및 전시회"),
    CULTURAL_HERITAGE("문화유산"),

    // TRANSPORTATION_MOBILITY
    RIDE_SHARING("차량 공유"),
    ELECTRIC_VEHICLE_TECH("전기차 관련 기술"),
    DRONES("드론"),

    // FINANCE_INSURANCE_FINTECH
    DIGITAL_FINANCE_PAYMENTS("디지털 금융 및 지급 결제"),
    FINANCIAL_MANAGEMENT("재무관리"),
    INSURE_TECH("인슈어테크");

    private final String koreanName;

    SubCategory(String koreanName) {
        this.koreanName = koreanName;
    }

    public String getKoreanName() {
        return koreanName;
    }

    public static SubCategory from(String koreanName) {
        return Arrays.stream(values())
                .filter(val -> koreanName.equals(val.koreanName))
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.SUB_CATEGORY_NOT_FOUND));
    }
}
