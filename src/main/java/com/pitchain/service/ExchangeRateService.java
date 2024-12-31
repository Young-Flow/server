package com.pitchain.service;

import com.pitchain.dto.res.ExchangeRateRes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateService {

    @Value("${koreaexim.url}")
    private String url;

    @Value("${koreaexim.authKey}")
    private String authKey;

    @Value("${koreaexim.data}")
    private String data;


    /**
     * 한국수출입은행으로부터 통화별 일환율 조회
     * @return Map<String, String>
     */
    @Cacheable(value = "map", cacheManager = "redisCacheManager", key = "{#root.methodName, #todayDate}")
    public Map<String, String> getExchangeRateMap(String todayDate) {
        RestTemplate restTemplate = new RestTemplate();
        String uri = generateRequestURI();
        ExchangeRateRes[] exchangeRateList = restTemplate.getForObject(uri, ExchangeRateRes[].class);

        Map<String, String> exchangeRateMap = new HashMap<>();
        for (ExchangeRateRes exchangeRate : exchangeRateList) {
            String curUnit = exchangeRate.getCur_unit();  //통화코드
            String dealBasR = exchangeRate.getDeal_bas_r();  //매매 기준율

            exchangeRateMap.put(curUnit, dealBasR);
        }

        return exchangeRateMap;
    }

    private String generateRequestURI() {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("authkey", authKey)
                .queryParam("data", data)
                .build().toString();
    }
}
