package com.pitchain.service;

import com.pitchain.dto.res.ExchangeRateRes;
import com.pitchain.redis.RedisExchangeRateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ExchangeRateService {

    @Value("${koreaexim.url}")
    private String url;

    @Value("${koreaexim.authKey}")
    private String authKey;

    @Value("${koreaexim.data}")
    private String data;

    @Value("${koreaexim.updateTime.hour}")
    private int updateHour;

    @Value("${koreaexim.updateTime.min}")
    private int updateMin;

    private final RedisExchangeRateUtil redisExchangeRateUtil;
    private final RestTemplate restTemplate;

    /**
     * 가장 최근의 통화별 일환율 조회
     * 일환율 업데이트 전이면 어제 환율 조회
     * 일환율 업데이트 후면 오늘 환율 조회
     *
     * @return
     */
    public Map<String, String> getLatestExchangeRateMap() {
        if (isBeforeUpdateTime()) {
            return redisExchangeRateUtil.getExchangeRateMap(getYesterdayDate());
        }
        return redisExchangeRateUtil.getExchangeRateMap(getTodayDate());
    }

    /**
     * 한국수출입은행 현재환율 API 호출을 통한 통화별 일환율 조회 및 DB에 저장
     * 비영업일이면 어제 일환율을 저장
     * 매일 11시 30분 업데이트
     *
     * @return Map<String, String>
     */
    @Scheduled(cron = "${koreaexim.updateTime.cron}")
    public void updateExchangeRateMap() {
        String uri = generateRequestURI();
        ExchangeRateRes[] exchangeRateList = restTemplate.getForObject(uri, ExchangeRateRes[].class);

        Map<String, String> usdBasedExchangeRateMap = getExchangeRateMap(exchangeRateList);

        redisExchangeRateUtil.setExchangeRateMap(getTodayDate(), usdBasedExchangeRateMap);
    }

    private Map<String, String> getExchangeRateMap(ExchangeRateRes[] exchangeRateList) {
        if (isNonBusinessDay(exchangeRateList)) {
            return redisExchangeRateUtil.getExchangeRateMap(getYesterdayDate());
        }

        Map<String, String> krwBasedExchangeRateMap = convertListToMap(exchangeRateList);
        return convertToUsdBasedExchageRateMap(krwBasedExchangeRateMap);
    }

    private Map<String, String> convertToUsdBasedExchageRateMap(Map<String, String> krwBasedExchangeRateMap) {
        Map<String, String> usdBasedExchangeRateMap = new HashMap<>();
        String krw2Usd = krwBasedExchangeRateMap.get("USD");

        for (String currency : krwBasedExchangeRateMap.keySet()) {
            String exchangeRate = krwBasedExchangeRateMap.get(currency);
            String usdBasedExchangeRate = String.format("%.2f", Double.parseDouble(krw2Usd) / Double.parseDouble(exchangeRate));

            usdBasedExchangeRateMap.put(currency, usdBasedExchangeRate);
        }

        return usdBasedExchangeRateMap;
    }

    private Map<String, String> convertListToMap(ExchangeRateRes[] exchangeRateList) {
        Map<String, String> exchangeRateMap = new HashMap<>();
        for (ExchangeRateRes exchangeRate : exchangeRateList) {
            String curUnit = exchangeRate.getCur_unit();  //통화코드
            String dealBasR = exchangeRate.getDeal_bas_r().replace(",", "");  //매매 기준율

            exchangeRateMap.put(curUnit, dealBasR);
        }

        return exchangeRateMap;
    }

    private String getTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }

    private String getYesterdayDate() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return yesterday.format(formatter);
    }

    private String generateRequestURI() {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("authkey", authKey)
                .queryParam("data", data)
                .build().toString();
    }

    private boolean isNonBusinessDay(ExchangeRateRes[] exchangeRateList) {
        return exchangeRateList.length == 0;
    }

    private boolean isBeforeUpdateTime() {
        LocalTime currentTime = LocalTime.now();
        LocalTime updateTime = LocalTime.of(updateHour, updateMin);
        return currentTime.isBefore(updateTime);
    }

}
