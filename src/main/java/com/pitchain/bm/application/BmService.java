package com.pitchain.bm.application;

import com.pitchain.bm.presentation.req.BmCreateReq;
import com.pitchain.bm.presentation.req.BmUpdateReq;
import com.pitchain.bm.application.res.BmDetailRes;
import com.pitchain.common.security.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BmService {
    private final BmCommandService bmCommandService;

    @Transactional
    public void createBm(MemberDetails memberDetails, BmCreateReq bmCreateReq, MultipartFile descImg) {
        bmCommandService.createBm(memberDetails, bmCreateReq, descImg);
    }

    @Transactional
    public BmDetailRes getBmDetail(MemberDetails memberDetails, Long bmId) {
        return bmCommandService.getBmDetail(memberDetails, bmId);
    }

    @Transactional
    public void updateBm(MemberDetails memberDetails, Long bmId, BmUpdateReq bmUpdateReq, MultipartFile descImg) {
        bmCommandService.updateBm(memberDetails, bmId, bmUpdateReq, descImg);
    }

    @Transactional
    public void updatePtImgs(MemberDetails memberDetails, Long bmId, List<String> uploadPtImgKeys) {
        bmCommandService.updatePtImgs(memberDetails, bmId, uploadPtImgKeys);
    }

    @Transactional
    public void deleteBm(MemberDetails memberDetails, Long bmId) {
        bmCommandService.deleteBm(memberDetails, bmId);
    }
}
