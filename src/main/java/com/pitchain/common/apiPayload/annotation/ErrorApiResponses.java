package com.pitchain.common.apiPayload.annotation;


import com.pitchain.common.apiPayload.ErrorStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorApiResponses {
    ErrorStatus[] value();
}
