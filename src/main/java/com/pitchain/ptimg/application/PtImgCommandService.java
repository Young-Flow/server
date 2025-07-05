package com.pitchain.ptimg.application;

import com.pitchain.bm.domain.Bm;
import com.pitchain.ptimg.domain.PtImg;
import com.pitchain.common.application.EntityFacade;
import com.pitchain.ptimg.infrastructure.PtImgRepository;
import com.pitchain.upload.application.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static software.amazon.awssdk.utils.CollectionUtils.isNullOrEmpty;

@RequiredArgsConstructor
@Service
public class PtImgCommandService {
    private final EntityFacade entityFacade;
    private final PtImgRepository ptImgRepository;
    private final S3Service s3Service;

    @Transactional
    public void updatePtImgs(Long bmId, List<String> uploadPtImgKeys) {
        Bm bm = entityFacade.getBm(bmId);
        List<PtImg> OldPtImgs = ptImgRepository.findAllByBmId(bmId);

        deleteAll(OldPtImgs);

        if (!isNullOrEmpty(uploadPtImgKeys)) {
            saveAll(uploadPtImgKeys, bm);
        }
    }

    private void deleteAll(List<PtImg> OldPtImgs) {
        OldPtImgs.forEach(ptImg -> s3Service.deleteImg(ptImg.getImgKey()));
        ptImgRepository.deleteAll(OldPtImgs);
    }

    private void saveAll(List<String> uploadPtImgKeys, Bm bm) {
        List<PtImg> newPtImgs = uploadPtImgs(uploadPtImgKeys, bm);
        ptImgRepository.saveAll(newPtImgs);
    }

    private List<PtImg> uploadPtImgs(List<String> ptImgKeys, Bm bm) {
        List<PtImg> uploadPtImgs = new ArrayList<>();
        for (int serialNum = 0; ptImgKeys != null && serialNum < ptImgKeys.size(); serialNum++) {
            String uploadFileKey = ptImgKeys.get(serialNum);
            PtImg ptImg = new PtImg(bm, serialNum, uploadFileKey);
            uploadPtImgs.add(ptImg);
        }
        return uploadPtImgs;
    }
}
