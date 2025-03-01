package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.dto.req.CreateCompanyReq;
import com.pitchain.dto.req.UpdateCompanyReq;
import com.pitchain.dto.res.CompanyDetailRes;
import com.pitchain.entity.Company;
import com.pitchain.entity.Member;
import com.pitchain.repository.CompanyRepository;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class CompanyService {

    private final EntityFacade entityFacade;
    private final S3Service s3Service;
    private final CompanyRepository companyRepository;

    public void createCompany(Long memberId, CreateCompanyReq req, MultipartFile logoImg) {
        Member member = entityFacade.getMember(memberId);

        String logoImgKey = s3Service.uploadFile(logoImg, S3UploadTarget.COMPANY_LOGO);

        Company company = req.createCompany(logoImgKey, member);

        companyRepository.save(company);
    }

    @Transactional(readOnly = true)
    public CompanyDetailRes getCompanyDetail(Long memberId, Long companyId) {
        Member member = entityFacade.getMember(memberId);

        Company company = entityFacade.getCompany(companyId);

        return CompanyDetailRes.createRes(company);
    }

    @Transactional(readOnly = true)
    public List<CompanyDetailRes> getMyCompanyDetails(Long memberId) {
        Member member = entityFacade.getMember(memberId);

        List<Company> companies = companyRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.COMPANY_NOT_FOUND));

        return companies.stream().map(CompanyDetailRes::createRes).toList();
    }

    public void updateCompanyInfo(Long memberId, Long companyId, UpdateCompanyReq req) {
        Member member = entityFacade.getMember(memberId);
        Company company = entityFacade.getCompany(companyId);

        validateCompanyOwner(company, member);

        company.updateCompanyInfo(req.name(), req.address());
    }

    public void updateCompanyLogoImg(Long memberId, Long companyId, MultipartFile logoImg) {
        Member member = entityFacade.getMember(memberId);
        Company company = entityFacade.getCompany(companyId);

        validateCompanyOwner(company, member);

        if (company.hasLogoImg())
            s3Service.deleteImg(company.getLogoImgKey());

        String logoImgKey = s3Service.uploadFile(logoImg, S3UploadTarget.COMPANY_LOGO);
        company.updateLogoImgKey(logoImgKey);
    }

    public void deleteCompany(Long memberId, Long companyId) {
        Member member = entityFacade.getMember(memberId);
        Company company = entityFacade.getCompany(companyId);

        validateCompanyOwner(company, member);

        if (company.hasLogoImg())
            s3Service.deleteImg(company.getLogoImgKey());

        companyRepository.delete(company);
    }

    private static void validateCompanyOwner(Company company, Member member) {
        if (!company.isOwner(member)) {
            throw new GeneralHandler(ErrorStatus.MEMBER_FORBIDDEN);
        }
    }
}
