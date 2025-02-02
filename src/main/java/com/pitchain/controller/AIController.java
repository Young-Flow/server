package com.pitchain.controller;

import com.pitchain.dto.res.MemberPreferenceInfoRes;
import com.pitchain.dto.res.PreferenceInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class AIController {

    @Operation(summary = "AI 서버로 유저 선호도 정보 전송 / 개발용")
    @GetMapping("/dev-model")
    public String devAI() {
        List<MemberPreferenceInfoRes> memberPreferenceInfoResList = List.of(
                new MemberPreferenceInfoRes(1L, List.of(
                        new PreferenceInfoRes(1L, 10, true, false),
                        new PreferenceInfoRes(2L, 20, false, true)
                )),
                new MemberPreferenceInfoRes(2L, List.of(
                        new PreferenceInfoRes(3L, 30, true, true),
                        new PreferenceInfoRes(4L, 40, false, false)
                ))
        );

        new RestTemplate().postForObject("http://localhost:8000/models", memberPreferenceInfoResList, Void.class);
        return "ok";
    }
}
