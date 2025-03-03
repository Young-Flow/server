package com.pitchain.service;

import com.pitchain.common.apiPayload.statusEnums.ErrorStatus;
import com.pitchain.common.constant.S3UploadTarget;
import com.pitchain.common.exception.GeneralHandler;
import com.pitchain.controller.UpdatePasswordReq;
import com.pitchain.dto.req.CreateCompanyReq;
import com.pitchain.dto.req.LoginCompanyReq;
import com.pitchain.dto.req.UpdateCompanyReq;
import com.pitchain.dto.res.CompanyDetailRes;
import com.pitchain.dto.res.LoginRes;
import com.pitchain.entity.Company;
import com.pitchain.jwt.TokenUtil;
import com.pitchain.repository.CompanyRepository;
import com.pitchain.repository.EntityFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class CompanyService {
    private final S3Service s3Service;
    private final EntityFacade entityFacade;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenUtil tokenUtil;

    public void createCompany(CreateCompanyReq req) {
        Optional<Company> optionalCompany = companyRepository.findByEmail(req.email());
        verifyEmailConflict(optionalCompany);

        Company company = req.createUnverifiedCompany();
        companyRepository.save(company);
    }

    public LoginRes loginCompany(LoginCompanyReq req) {
        Company company = companyRepository.findByEmail(req.email())
                .orElseThrow(() -> new GeneralHandler(ErrorStatus.COMPANY_NOT_FOUND));

        verifyPassword(req.password(), company.getPassword());

        String accessToken = tokenUtil.issueAccessToken(company.getId());
        String refreshToken = tokenUtil.issueRefreshToken(company.getId());

        return LoginRes.createRes(accessToken, refreshToken);
    }

    @Transactional(readOnly = true)
    public CompanyDetailRes getCompanyDetail(Long companyId) {
        Company company = entityFacade.getCompany(companyId);

        return CompanyDetailRes.createRes(company);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatedEmail(String email) {
        return companyRepository.findByEmail(email).isPresent();
    }

    public void updateEmail(Long companyId, String email) {
        Company company = entityFacade.getCompany(companyId);

        Optional<Company> optionalCompany = companyRepository.findByEmail(email);
        verifyEmailConflict(optionalCompany);

        company.updateEmail(email);
    }

    public void updatePassword(Long companyId, UpdatePasswordReq req) {
        Company company = entityFacade.getCompany(companyId);

        verifyPassword(req.originPassword(), company.getPassword());

        company.updatePassword(req.newPassword());
    }

    public void updateLogoImg(Long companyId, MultipartFile logoImg) {
        Company company = entityFacade.getCompany(companyId);

        if (company.hasLogoImg())
            s3Service.deleteImg(company.getLogoImgKey());

        String logoImgKey = s3Service.uploadFile(logoImg, S3UploadTarget.COMPANY_LOGO);
        company.updateLogoImgKey(logoImgKey);
    }

    public void updateCompanyInfo(Long companyId, UpdateCompanyReq req) {
        Company company = entityFacade.getCompany(companyId);

        company.updateCompanyInfo(req.name(), req.address());
    }

    public void deleteCompany(Long companyId) {
        Company company = entityFacade.getCompany(companyId);

        if (company.hasLogoImg())
            s3Service.deleteImg(company.getLogoImgKey());

        companyRepository.delete(company);
    }

    private static void verifyEmailConflict(Optional<Company> optionalCompany) {
        if (optionalCompany.isPresent())
            throw new GeneralHandler(ErrorStatus.COMPANY_EMAIL_CONFLICT);
    }

    private void verifyPassword(String inputPassword, String encodedPassword) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword))
            throw new GeneralHandler(ErrorStatus.COMPANY_PASSWORD_NOT_MATCHED);
    }
}
