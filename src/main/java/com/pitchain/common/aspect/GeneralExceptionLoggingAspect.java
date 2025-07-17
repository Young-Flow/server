package com.pitchain.common.aspect;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import com.pitchain.common.util.LoggingUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("prod")
@Slf4j
@Aspect
@Component
public class GeneralExceptionLoggingAspect {

    @Pointcut("execution(public * com.pitchain..*.application..*.*(..))")
    private void logPointcut() {}

    @AfterThrowing(value = "logPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, GeneralException exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();

        List<String> arguments = LoggingUtil.getArguments(joinPoint);
        String parameterMessage = LoggingUtil.getParameterMessage(arguments);

        ErrorStatus errorStatus = exception.getErrorStatus();
        MDC.put("httpStatus", String.valueOf(errorStatus.getHttpStatus()));

        log.error("[ERROR] POINT : {} || ERROR CODE : {} || ARGUMENTS : {}",
                className, errorStatus.getCode(), parameterMessage
        );
        log.error("[ERROR] FINAL POINT : {}", exception.getStackTrace()[0]);
        log.error("[ERROR] MESSAGE : {}", exception.getMessage());

        MDC.remove("httpStatus");
    }
}
