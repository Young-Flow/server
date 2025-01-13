package com.pitchain.common.apiPayload.statusEnums;

import com.pitchain.common.apiPayload.dto.ResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements ResponseStatus {

    // common
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // jwt
    MISSING_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "TOKEN4012", "Access Token이 존재하지 않습니다."),

    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4041", "존재하지 않는 사용자입니다."),
    MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "MEMBER4031", "사용자에게 권한이 없습니다."),

    // bm
    BM_NOT_FOUND(HttpStatus.NOT_FOUND, "BM4041", "존재하지 않는 BM입니다."),

    // sp
    SP_NOT_FOUND(HttpStatus.NOT_FOUND, "SP4041", "존재하지 않는 SP입니다."),

    // comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT4041", "존재하지 않는 댓글입니다."),

    // S3
    BAD_REQUEST_FILE(HttpStatus.BAD_REQUEST, "S3_40001", "잘못된 파일 데이터입니다."),
    INVALID_BUCKET_URL(HttpStatus.BAD_REQUEST, "S3_4002", "유효하지 않은 버킷 URL입니다."),
    UNAUTHORIZED_S3(HttpStatus.UNAUTHORIZED, "S3_4011", "S3 접근 인증에 실패했습니다."),
    FORBIDDEN_S3(HttpStatus.FORBIDDEN, "S3_4031", "S3 권한을 가지고 있지 않습니다."),
    FAIL_FILE_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5001", "S3에 파일 업로드를 실패했습니다."),
    UNAVAILABLE_S3(HttpStatus.SERVICE_UNAVAILABLE, "S3_5031", "S3 서버가 일시적으로 데이터를 처리할 수 없습니다."),

    // FILE
    INVALID_MIME_TYPE(HttpStatus.BAD_REQUEST, "FILE4001", "유효하지 않은 파일 형식입니다."),
    FAIL_STREAM_CONVERT(HttpStatus.BAD_REQUEST, "FILE4002", "스트림 변환에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ResponseDTO getDto() {
        return ResponseDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ResponseDTO getHttpStatusDto() {
        return ResponseDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
