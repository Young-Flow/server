package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.BmWithLikeDto;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.dto.res.PtImgRes;
import com.pitchain.entity.Member;
import com.pitchain.entity.PtImg;
import com.pitchain.repository.BmRepository;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.MyBmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class BmService {
    private final EntityFacade entityFacade;
    private final BmRepository bmRepository;
    private final MyBmRepository myBmRepository;

    @Transactional(readOnly = true)
    public BmDetailRes getBmDetail(Long memberId, Long bmId) {
        Member member = entityFacade.getMember(memberId);

        BmWithLikeDto bmWithLikeDto = bmRepository.getBmWithLikeDto(member.getId(), bmId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.BM_NOT_FOUND));

        List<PtImg> ptImgs = bmRepository.getPtImgsByBmId(bmWithLikeDto.getBm().getId());
        List<PtImgRes> ptImgResList = ptImgs.stream()
                .map(PtImgRes::createRes)
                .toList();

        long likeCnt = myBmRepository.countByBm(bmWithLikeDto.getBm());

        return BmDetailRes.createRes(bmWithLikeDto, likeCnt, ptImgResList);
    }
}
