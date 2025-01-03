package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.entity.Member;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final MemberRepository memberRepository;
    private final ExchangeRateService ExchangeRateService;

    public String calculateExchangeRate(Long memberId, Integer amount) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        String countryUnit = member.getCountry().getCountryUnit();  //사용자의 통화코드 조회

        Map<String, String> exchangeRateMap = ExchangeRateService.getExchangeRateMap(getTodayDate());  //오늘 통화코드 목록 조회
        double exchangeRate = getExchangeRate(countryUnit, exchangeRateMap);

        double calculatedAmount = amount / exchangeRate;
        return String.format("%.3f", calculatedAmount);
    }

    private double getExchangeRate(String countryUnit, Map<String, String> currencyExchangeMap) {
        String exchangeRate = currencyExchangeMap.get(countryUnit).replace(",", "");
        return Double.parseDouble(exchangeRate);
    }

    private String getTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }

}
