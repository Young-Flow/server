package com.pitchain.controller;

import com.pitchain.common.apiPayload.dto.CustomApiResponse;
import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "에러 테스트", description = "에러 테스트를 위한 컨트롤러")
@RestController("/dev-error")
public class ErrorController {

    @Operation(summary = "ConstraintViolationException 발생")
    @GetMapping("/constraint-violation")
    public void constraintViolationTest() {
        throw new ConstraintViolationException("테스트 ConstraintViolationException입니다", null);
    }

    @Operation(summary = "IllegalArgumentException 발생")
    @GetMapping("/illegal-argument")
    public void illegalArgumentTest() {
        throw new IllegalArgumentException("테스트 IllegalArgumentException입니다");
    }

    @Operation(summary = "GeneralHandler 발생")
    @GetMapping("/general-handler")
    public void generalExceptionTest() {
        throw new GeneralHandler(ErrorStatus.TEST_ERROR);
    }

    @Operation(summary = "MethodArgumentNotValidException 발생")
    @PostMapping("/method-argument-not-valid")
    public CustomApiResponse<String> methodArgumentNotValidTest(@Valid @RequestBody TestDto testDto) {
        return CustomApiResponse.onSuccess("빈 문자열을 입력하면 에러가 발생합니다.");
    }

    @Getter
    @Setter
    public static class TestDto {
        @NotBlank(message = "name은 필수 값입니다.")
        private String name;
    }
}
