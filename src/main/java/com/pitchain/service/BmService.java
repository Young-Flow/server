package com.pitchain.service;

import com.pitchain.dto.req.BmCreateReq;
import com.pitchain.dto.req.BmUpdateReq;
import com.pitchain.dto.res.BmDetailRes;
import com.pitchain.jwt.MemberDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BmService {
    private final BmCommandService bmCommandService;

    public void createBm(MemberDetails memberDetails, BmCreateReq bmCreateReq, MultipartFile descImg) {
        bmCommandService.createBm(memberDetails, bmCreateReq, descImg);
    }

    public BmDetailRes getBmDetail(MemberDetails memberDetails, Long bmId) {
        return bmCommandService.getBmDetail(memberDetails, bmId);
    }

    public void updateBm(MemberDetails memberDetails, Long bmId, BmUpdateReq bmUpdateReq, MultipartFile descImg) {
        bmCommandService.updateBm(memberDetails, bmId, bmUpdateReq, descImg);
    }

    public void updatePtImgs(MemberDetails memberDetails, Long bmId, List<String> uploadPtImgKeys) {
        bmCommandService.updatePtImgs(memberDetails, bmId, uploadPtImgKeys);
    }

    public void deleteBm(MemberDetails memberDetails, Long bmId) {
        bmCommandService.deleteBm(memberDetails, bmId);
    }
}
