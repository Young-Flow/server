package com.pitchain.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Country {

    ROK("KRW"),
    USA("USD");

    private final String countryUnit;
}
