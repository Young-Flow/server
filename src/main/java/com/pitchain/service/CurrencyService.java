package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.entity.Member;
import com.pitchain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final MemberRepository memberRepository;
    private final ExchangeRateService exchangeRateService;

    public String calculateExchangeRate(Long memberId, long amount) {
        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new GeneralHandler(ErrorStatus.MEMBER_NOT_FOUND));
        String countryUnit = member.getCountry().getCountryUnit();  //사용자의 통화코드 조회

        Map<String, String> latestExchangeRateMap = exchangeRateService.getLatestExchangeRateMap();
        double exchangeRate = getExchangeRate(countryUnit, latestExchangeRateMap);

        double calculatedAmount = amount / exchangeRate;
        return String.format("%.3f", calculatedAmount);
    }

    public String getExchangeRateUpdateDateTime() {
        Map<String, String> latestExchangeRateMap = exchangeRateService.getLatestExchangeRateMap();
        return latestExchangeRateMap.get("updateDateTime");
    }

    private double getExchangeRate(String countryUnit, Map<String, String> currencyExchangeMap) {
        String exchangeRate = currencyExchangeMap.get(countryUnit);
        return Double.parseDouble(exchangeRate);
    }

}
