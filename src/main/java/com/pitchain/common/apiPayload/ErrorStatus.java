package com.pitchain.common.apiPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@AllArgsConstructor
public enum ErrorStatus implements ResponseStatus {

    // common
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "common.bad-request"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "common.unauthorized"),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "common.forbidden"),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "common.internal-server-error"),


    // jwt
    TOKEN_MISSING(HttpStatus.BAD_REQUEST, "TOKEN4001", "token.missing"),
    TOKEN_UNVERIFIED(HttpStatus.UNAUTHORIZED, "TOKEN4011", "token.unverified"),

    // member
    INVALID_MEMBER_ROLE(HttpStatus.BAD_REQUEST, "MEMBER4001", "member.invalid-role"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4041", "member.not-found"),
    MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER4031", "member.forbidden"),

    // individual
    INDIVIDUAL_NOT_FOUND(HttpStatus.NOT_FOUND, "INDIVIDUAL4041", "individual.not-found"),

    // company
    COMPANY_PASSWORD_UNCONFIRMED(HttpStatus.BAD_REQUEST, "COMPANY4001", "company.password-unconfirmed"),
    COMPANY_FORBIDDEN(HttpStatus.FORBIDDEN, "COMPANY4031", "company.forbidden"),
    COMPANY_PASSWORD_NOT_MATCHED(HttpStatus.FORBIDDEN, "COMPANY4032", "company.password-not-matched"),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMPANY4041", "company.not-found"),
    COMPANY_EMAIL_CONFLICT(HttpStatus.CONFLICT, "COMPANY4091", "company.email-conflict"),

    // bm
    BM_NOT_FOUND(HttpStatus.NOT_FOUND, "BM4041", "bm.not-found"),

    // main category
    MAIN_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "MAIN_CATEGORY4041", "main-category.not-found"),

    // subCategory
    SUB_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "SUB_CATEGORY4041", "sub-category.not-found"),

    // sp
    SP_NOT_FOUND(HttpStatus.NOT_FOUND, "SP4041", "sp.not-found"),

    // comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4041", "comment.not-found"),

    // categoryPref
    CATEGORY_PREF_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_PREF4041", "category-pref.not-found"),

    // S3
    FAIL_S3_UPLOAD(HttpStatus.BAD_REQUEST, "S3_4001", "s3.fail-upload"),
    INVALID_BUCKET_URL(HttpStatus.BAD_REQUEST, "S3_4002", "s3.invalid-bucket-url"),

    // file
    INVALID_MIME_TYPE(HttpStatus.BAD_REQUEST, "FILE4001", "file.invalid-mime-type"),
    FAIL_STREAM_CONVERT(HttpStatus.BAD_REQUEST, "FILE4002", "file.fail-stream-convert")

    ;

    private final HttpStatus httpStatus;
    @Getter
    private final String code;
    private final String messageKey;

    @Override
    public ErrorResponseDTO getCustomResponseDTO(MessageSource messageSource) {
        return new ErrorResponseDTO(httpStatus, code, messageSource.getMessage(messageKey, null, Locale.KOREA));
    }
}
