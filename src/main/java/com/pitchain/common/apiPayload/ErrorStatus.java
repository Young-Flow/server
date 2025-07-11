package com.pitchain.common.apiPayload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;

import java.util.Locale;

@AllArgsConstructor
public enum ErrorStatus implements ResponseStatus {

    // common
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_400_1", "common.bad-request"),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_401_1", "common.unauthorized"),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_403_1", "common.forbidden"),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_500_1", "common.internal-server-error"),

    // discord
    DISCORD_NO_WEBHOOK_STRATEGY(HttpStatus.BAD_REQUEST, "NO_WEBHOOK_STRATEGY_500_1", "discord.no-webhook-strategy"),

    // sse
    SSE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "SSE_500_1", "sse.send-failed"),

    // jwt
    TOKEN_MISSING(HttpStatus.BAD_REQUEST, "TOKEN_400_1", "token.missing"),
    TOKEN_UNVERIFIED(HttpStatus.UNAUTHORIZED, "TOKEN_401_1", "token.unverified"),

    // member
    INVALID_MEMBER_ROLE(HttpStatus.BAD_REQUEST, "MEMBER_400_1", "member.invalid-role"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER_404_1", "member.not-found"),
    MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER_403_1", "member.forbidden"),
    MEMBER_EMAIL_CONFLICT(HttpStatus.CONFLICT, "MEMBER_409_1", "member.email-conflict"),

    // individual
    INDIVIDUAL_NOT_FOUND(HttpStatus.NOT_FOUND, "INDIVIDUAL_404_1", "individual.not-found"),

    // company
    COMPANY_PASSWORD_UNCONFIRMED(HttpStatus.BAD_REQUEST, "COMPANY_400_1", "company.password-unconfirmed"),
    COMPANY_FORBIDDEN(HttpStatus.FORBIDDEN, "COMPANY_403_1", "company.forbidden"),
    COMPANY_PASSWORD_NOT_MATCHED(HttpStatus.FORBIDDEN, "COMPANY_403_2", "company.password-not-matched"),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMPANY_404_1", "company.not-found"),

    // bm
    BM_NOT_FOUND(HttpStatus.NOT_FOUND, "BM_404_1", "bm.not-found"),

    // main category
    MAIN_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "MAIN_CATEGORY_404_1", "main-category.not-found"),

    // subCategory
    SUB_CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "SUB_CATEGORY_404_1", "sub-category.not-found"),

    // sp
    SP_STATUS_CONVERSION_FAILED(HttpStatus.BAD_REQUEST, "SP_400_1", "sp.status-conversion-failed"),
    SP_NOT_FOUND(HttpStatus.NOT_FOUND, "SP_404_1", "sp.not-found"),

    // comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_404_1", "comment.not-found"),

    // categoryPref
    CATEGORY_PREF_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_PREF_404_1", "category-pref.not-found"),

    // notification
    NOTIFICATION_TYPE_CONVERSION_FAILED(HttpStatus.BAD_REQUEST, "NOTIFICATION_400_1", "notification.status-conversion-failed"),

    // S3
    FAIL_S3_UPLOAD(HttpStatus.BAD_REQUEST, "S3_400_1", "s3.fail-upload"),
    INVALID_BUCKET_URL(HttpStatus.BAD_REQUEST, "S3_400_2", "s3.invalid-bucket-url"),

    // file,
    INVALID_MIME_TYPE(HttpStatus.BAD_REQUEST, "FILE_400_1", "file.invalid-mime-type"),
    FAIL_STREAM_CONVERT(HttpStatus.BAD_REQUEST, "FILE_400_2", "file.fail-stream-convert"), ;

    @Getter
    private final HttpStatus httpStatus;
    @Getter
    private final String code;
    private final String messageKey;

    @Override
    public ErrorResponseDTO getCustomResponseDTO(MessageSource messageSource) {
        return new ErrorResponseDTO(httpStatus, code, messageSource.getMessage(messageKey, null, Locale.KOREA));
    }
}
