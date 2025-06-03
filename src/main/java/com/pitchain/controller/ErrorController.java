package com.pitchain.controller;

import com.pitchain.common.apiPayload.ErrorStatus;
import com.pitchain.common.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "에러 테스트", description = "에러 테스트를 위한 컨트롤러")
@RequestMapping("/dev-error")
@Validated
@RestController
public class ErrorController {

    @Operation(summary = "ConstraintViolationException 발생")
    @GetMapping("/constraint-violation")
    public void constraintViolationTest(@NotBlank(message = "name은 필수입니다.") String name) {
    }

    @Operation(summary = "GeneralException 발생")
    @GetMapping("/general-exception")
    public void generalExceptionTest() {
        throw new GeneralException(ErrorStatus._BAD_REQUEST);
    }

    @Operation(summary = "MethodArgumentNotValidException 발생")
    @PostMapping("/method-argument-not-valid")
    public String methodArgumentNotValidTest(@Valid @RequestBody TestDto testDto) {
        return "빈 문자열을 입력하면 에러가 발생합니다.";
    }

    @Getter
    @Setter
    public static class TestDto {
        @NotBlank(message = "name은 필수 값입니다.")
        private String name;
    }
}
