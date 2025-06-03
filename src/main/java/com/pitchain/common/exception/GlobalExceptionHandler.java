package com.pitchain.common.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.pitchain.common.apiPayload.CustomResponse;
import com.pitchain.common.apiPayload.ErrorResponseDTO;
import com.pitchain.common.apiPayload.ErrorStatus;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<CustomResponse> handleGeneralException(GeneralException e) {
        e.printStackTrace();

        ErrorResponseDTO error = e.getErrorResponse(messageSource);
        CustomResponse customResponse = CustomResponse.onFailure(error.getCode(), error.getMessage());

        return ResponseEntity
                .status(error.getHttpStatus())
                .body(customResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        e.printStackTrace();

        ObjectError first = e.getBindingResult().getAllErrors().stream().findFirst().get();
        String errorMessage = first.getDefaultMessage();

        CustomResponse customResponse = CustomResponse.onFailure(ErrorStatus._BAD_REQUEST.name(), errorMessage);

        return ResponseEntity
                .status(e.getStatusCode())
                .body(customResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomResponse> handleConstraintViolationException(ConstraintViolationException e) {
        e.printStackTrace();

        String errorMessage = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getMessage())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ConstraintViolationException 추출 도중 에러 발생"));

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        CustomResponse customResponse = CustomResponse.onFailure(httpStatus.name(), errorMessage);

        return ResponseEntity
                .status(httpStatus)
                .body(customResponse);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<CustomResponse> handleJWTVerificationException(JWTVerificationException e) {
        ErrorResponseDTO error = ErrorStatus.TOKEN_UNVERIFIED.getCustomResponseDTO(messageSource);
        CustomResponse customResponse = CustomResponse.onFailure(error.getCode(), error.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(customResponse);
    }
}
