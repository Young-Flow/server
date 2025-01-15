package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.SpWithLikeDto;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.repository.SpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class SpService {
    private final SpRepository spRepository;

    @Transactional(readOnly = true)
    public List<SpDetailRes> getSpDetails(Long memberId) {
        List<SpWithLikeDto> spWithLikeDtos = spRepository.findAllWithLike(memberId);
        return spWithLikeDtos.stream()
                .map(spWithLikeDto -> {
                    Sp sp = spWithLikeDto.getSp();
                    long likeCnt = myBmRepository.countByBm(sp.getBm());
                    return SpDetailRes.createRes(spWithLikeDto, likeCnt);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public SpDetailRes getSpDetail(Long memberId, Long spId) {
        SpWithLikeDto spWithLikeDto = spRepository.findSpWithLike(memberId, spId)
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.SP_NOT_FOUND));

        long likeCnt = myBmRepository.countByBm(spWithLikeDto.getSp().getBm());

        return SpDetailRes.createRes(spWithLikeDto, likeCnt);
    }
}
