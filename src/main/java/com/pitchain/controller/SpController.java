package com.pitchain.controller;

import com.pitchain.common.annotation.RequiredRole;
import com.pitchain.common.constant.MemberRole;
import com.pitchain.common.entity.InfinityScrollRes;
import com.pitchain.dto.req.SpCreateReq;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.jwt.MemberDetails;
import com.pitchain.service.SpService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class SpController {
    private final SpService spService;

    @RequiredRole(MemberRole.COMPANY)
    @Operation(summary = "SP 생성")
    @PostMapping(value = "/bms/{bmId}/sps", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createSp(@AuthenticationPrincipal MemberDetails memberDetails,
                         @PathVariable Long bmId,
                         @Valid @RequestPart SpCreateReq spCreateReq,
                         @RequestPart MultipartFile thumbnailImg) {
        spService.createSp(memberDetails, spCreateReq, thumbnailImg);
    }

    @Operation(summary = "SP 리스트 조회")
    @GetMapping("/sps/all")
    public List<SpDetailRes> getSpDetails(@AuthenticationPrincipal MemberDetails memberDetails) {
        List<SpDetailRes> spDetailResList = spService.getSpDetails(memberDetails);
        return spDetailResList;
    }

    @Operation(summary = "카테고리별 SP 리스트 조회", description = "첫 조회 시 쿼리 파라미터에 lastSpId를 포함시키지 않습니다.")
    @GetMapping("/sps")
    public InfinityScrollRes<SpDetailRes> getSpDetailsFilteredCategory(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                       @RequestParam(required = false) String mainCategoryInKorean,
                                                                       @RequestParam(required = false) Long lastSpId,
                                                                       @RequestParam(defaultValue = "10") int size) {
        InfinityScrollRes<SpDetailRes> spDetailResList = spService.getSpDetailsFilteredCategory(memberDetails, mainCategoryInKorean, lastSpId, size);
        return spDetailResList;
    }

    @Operation(summary = "SP 상세 조회")
    @GetMapping("/bms/{bmId}/sps/{spId}")
    public SpDetailRes getSpDetail(@AuthenticationPrincipal MemberDetails memberDetails,
                                   @PathVariable Long bmId,
                                   @PathVariable Long spId) {
        SpDetailRes spDetailRes = spService.getSpDetail(memberDetails, spId);
        return spDetailRes;
    }

    @RequiredRole(MemberRole.COMPANY)
    @Operation(summary = "SP 수정")
    @PutMapping(value = "/bms/{bmId}/sps/{spId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateSp(@AuthenticationPrincipal MemberDetails memberDetails,
                         @PathVariable Long spId,
                         @PathVariable Long bmId,
                         @NotBlank @RequestPart String name,
                         @RequestPart(required = false) MultipartFile thumbnailImg) {
        spService.updateSp(memberDetails, spId, name, thumbnailImg);
    }

    @RequiredRole(MemberRole.COMPANY)
    @Operation(summary = "SP 삭제")
    @DeleteMapping("/bms/{bmId}/sps/{spId}")
    public void deleteSp(@AuthenticationPrincipal MemberDetails memberDetails,
                         @PathVariable Long bmId,
                         @PathVariable Long spId) {
        spService.deleteSp(memberDetails, spId);
    }
}
