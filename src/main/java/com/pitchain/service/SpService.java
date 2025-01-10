package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.res.SpDetailRes;
import com.pitchain.entity.Sp;
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
    public List<SpDetailRes> getSpDetails() {
        List<Sp> sps = spRepository.findAll();
        return sps.stream().map(SpDetailRes::createRes).toList();
    }

    @Transactional(readOnly = true)
    public SpDetailRes getSpDetail(Long spId) {
        Sp sp = spRepository.findById(spId).orElseThrow(
                () -> new GeneralHandler(ErrorStatus.SP_NOT_FOUND)
        );

        return SpDetailRes.createRes(sp);
    }
}
