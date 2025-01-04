package com.pitchain.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class RedisExchangeRateUtil {
    private static final String REDIS_KEY_PREFIX = "ExchangeRateMap";
    private final RedisTemplate<String, Map<String,String>> exchangeRateTemplate;

    public void setExchangeRateMap(String key, Map<String,String> value) {
        exchangeRateTemplate.opsForValue().set(REDIS_KEY_PREFIX + key, value);
    }

    public Map<String,String> getExchangeRateMap(String key) {
        return exchangeRateTemplate.opsForValue().get(REDIS_KEY_PREFIX + key);
    }

}
