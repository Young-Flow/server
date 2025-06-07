package com.pitchain.service;

import com.pitchain.entity.Bm;
import com.pitchain.entity.PtImg;
import com.pitchain.repository.EntityFacade;
import com.pitchain.repository.PtImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static software.amazon.awssdk.utils.CollectionUtils.isNullOrEmpty;

@RequiredArgsConstructor
@Service
public class PtImgService {
    private final EntityFacade entityFacade;
    private final PtImgRepository ptImgRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public List<PtImg> getPtImgsByBmId(Long bmId) {
        Bm bm = entityFacade.getBm(bmId);

        return ptImgRepository.findAllByBmId(bm.getId());
    }

    @Transactional
    public void update(Long bmId, List<String> uploadPtImgKeys) {
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
