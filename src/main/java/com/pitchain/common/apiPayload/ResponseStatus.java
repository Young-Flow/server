package com.pitchain.common.apiPayload;

import org.springframework.context.MessageSource;

public interface ResponseStatus {
    ErrorResponseDTO getCustomResponseDTO(MessageSource messageSource);
}
