package com.pitchain.common.exception;

import com.pitchain.common.apiPayload.ErrorResponseDTO;
import com.pitchain.common.apiPayload.ErrorStatus;
import lombok.Getter;
import org.springframework.context.MessageSource;

@Getter
public class GeneralException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public GeneralException(ErrorStatus errorStatus) {
        super(errorStatus.getCode());
        this.errorStatus = errorStatus;
    }

    public ErrorResponseDTO getErrorResponse(MessageSource messageSource) {
        return this.errorStatus.getCustomResponseDTO(messageSource);
    }
}