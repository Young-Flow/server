package com.pitchain.ptimg.application;

import com.pitchain.ptimg.domain.PtImg;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PtImgService {
    private final PtImgCommandService ptImgCommandService;
    private final PtImgQueryService ptImgQueryService;

    @Transactional(readOnly = true)
    public List<PtImg> getPtImgsByBmId(Long bmId) {
        return ptImgQueryService.getPtImgsByBmId(bmId);
    }

    @Transactional
    public void update(Long bmId, List<String> uploadPtImgKeys) {
        ptImgCommandService.updatePtImgs(bmId, uploadPtImgKeys);
    }

}
