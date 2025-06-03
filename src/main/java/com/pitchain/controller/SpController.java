package com.pitchain.controller;

import com.pitchain.common.entity.InfinityScrollRes;
import com.pitchain.dto.req.CreateSpReq;
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
@RequestMapping("/sps")
@RestController
public class SpController {
    private final SpService spService;

    @Operation(summary = "SP 생성")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void createSp(@AuthenticationPrincipal MemberDetails memberDetails,
                         @Valid @RequestPart CreateSpReq createSpReq,
                         @RequestPart MultipartFile spVideo,
                         @RequestPart MultipartFile thumbnailImg) {
        spService.createSp(memberDetails, createSpReq, spVideo, thumbnailImg);
    }

    @Operation(summary = "SP 리스트 조회")
    @GetMapping
    public List<SpDetailRes> getSpDetails(@AuthenticationPrincipal MemberDetails memberDetails) {
        List<SpDetailRes> spDetailResList = spService.getSpDetails(memberDetails);
        return spDetailResList;
    }

    @Operation(summary = "카테고리별 SP 리스트 조회", description = "첫 조회 시 쿼리 파라미터에 lastSpId를 포함시키지 않습니다.")
    @GetMapping("/category")
    public InfinityScrollRes<SpDetailRes> getSpDetailsFilteredCategory(@AuthenticationPrincipal MemberDetails memberDetails,
                                                                       @RequestParam(required = false) String mainCategoryInKorean,
                                                                       @RequestParam(required = false) Long lastSpId,
                                                                       @RequestParam(defaultValue = "10") int size) {
        InfinityScrollRes<SpDetailRes> spDetailResList = spService.getSpDetailsFilteredCategory(memberDetails, mainCategoryInKorean, lastSpId, size);
        return spDetailResList;
    }

    @Operation(summary = "SP 상세 조회")
    @GetMapping("/{spId}")
    public SpDetailRes getSpDetail(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable Long spId) {
        SpDetailRes spDetailRes = spService.getSpDetail(memberDetails, spId);
        return spDetailRes;
    }

    @Operation(summary = "SP 수정")
    @PutMapping(value = "/{spId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public void updateSp(@AuthenticationPrincipal MemberDetails memberDetails,
                         @PathVariable Long spId,
                         @NotBlank @RequestPart String name,
                         @RequestPart(required = false) MultipartFile spVideo,
                         @RequestPart(required = false) MultipartFile thumbnailImg) {
        spService.updateSp(memberDetails, spId, name, spVideo, thumbnailImg);
    }

    @Operation(summary = "SP 삭제")
    @DeleteMapping("/{spId}")
    public void deleteSp(@AuthenticationPrincipal MemberDetails memberDetails, @PathVariable Long spId) {
        spService.deleteSp(memberDetails, spId);
    }
}
