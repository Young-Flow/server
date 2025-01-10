package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.BmWithLikeDto;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.dto.res.PtImgRes;
import com.pitchain.repository.BmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BmService {
    private final BmRepository bmRepository;

    @Transactional(readOnly = true)
    public BmDetailRes getBmDetail(Long memberId, Long bmId) {
        BmWithLikeDto bmWithLikeDto = bmRepository.getBmWithLikeDto(memberId, bmId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.BM_NOT_FOUND));

        List<PtImgRes> ptImgResList = bmWithLikeDto.getBm().getPtImgs().stream()
                .map(PtImgRes::createRes)
                .toList();

        return BmDetailRes.createRes(bmWithLikeDto, ptImgResList);
    }
}
