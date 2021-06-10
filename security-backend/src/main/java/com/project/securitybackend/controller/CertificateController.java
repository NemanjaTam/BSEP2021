package com.project.securitybackend.controller;

import com.project.securitybackend.dto.request.EmailRequestDTO;
import com.project.securitybackend.dto.response.CertificateResponseDTO;
import com.project.securitybackend.dto.response.OCSPResponse;
import com.project.securitybackend.service.definition.ICertificateService;
import com.project.securitybackend.service.definition.IOCSPService;
import com.project.securitybackend.util.enums.CertificateType;
import com.project.securitybackend.util.exceptions.CertificateExceptions.NoValidCertificatesException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "SpellCheckingInspection", "DuplicatedCode"})
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final ICertificateService _certificateService;
    private final IOCSPService _ocspService;

    public CertificateController(ICertificateService certificateService, IOCSPService ocspService) {
        _certificateService = certificateService;
        _ocspService = ocspService;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('VIEW_CERTIFICATES')")
    public List<CertificateResponseDTO> getAllValidCertificates() {
        List<X509Certificate> endUserCertificates = _certificateService.getAllActiveEndUserCertificates();
        List<X509Certificate> CACertificates = _certificateService.getAllActiveIntermediateCertificates();
        List<X509Certificate> rootCertificates = _certificateService.getAllActiveRootCertificates();

        List<CertificateResponseDTO> retList = new ArrayList<>();
        retList.addAll(_certificateService.listToDTO(CertificateType.ROOT, rootCertificates));
        retList.addAll(_certificateService.listToDTO(CertificateType.INTERMEDIATE, CACertificates));
        retList.addAll(_certificateService.listToDTO(CertificateType.END_USER, endUserCertificates));

        if(retList.isEmpty()){
            throw new NoValidCertificatesException("");
        }

        return retList;
    }

    @GetMapping("/end-user")
    @PreAuthorize("hasAuthority('VIEW_CERTIFICATES')")
    public List<CertificateResponseDTO> getAllEndUserCertificates() {
        List<X509Certificate> certificateList = _certificateService.getAllActiveEndUserCertificates();

        if(certificateList.isEmpty()){
            throw new NoValidCertificatesException("end-user");
        }

        return _certificateService.listToDTO(CertificateType.END_USER, certificateList);
    }

    @GetMapping("/ca")
    @PreAuthorize("hasAuthority('VIEW_CERTIFICATES')")
    public List<CertificateResponseDTO> getAllValidCACertificates() {
        List<X509Certificate> intermediateCertificates = _certificateService.getAllActiveIntermediateCertificates();
        List<X509Certificate> rootCertificates = _certificateService.getAllActiveRootCertificates();

        List<CertificateResponseDTO> retList = new ArrayList<>();
        retList.addAll(_certificateService.listToDTO(CertificateType.ROOT, rootCertificates));
        retList.addAll(_certificateService.listToDTO(CertificateType.INTERMEDIATE,intermediateCertificates));

        if(retList.isEmpty()){
            throw new NoValidCertificatesException("CA");
        }

        return retList;
    }

    @GetMapping("/root")
    @PreAuthorize("hasAuthority('VIEW_CERTIFICATES')")
    public List<CertificateResponseDTO> getAllRootCertificates(){
        List<X509Certificate> certificateList = _certificateService.getAllActiveRootCertificates();
        return _certificateService.listToDTO(CertificateType.ROOT, certificateList);
    }

    @PostMapping("/download")
    @PreAuthorize("hasAuthority('DOWNLOAD_CERTIFICATE')")
    public ResponseEntity<Object> downloadCertificate(@RequestBody EmailRequestDTO request){
        return _certificateService.downloadCertificate(request);
    }

    @PostMapping("/file-name")
    @PreAuthorize("hasAuthority('VIEW_CERTIFICATES')")
    public List<String> getFileName(@RequestBody EmailRequestDTO request){
        return _certificateService.getFileName(request.getEmail());
    }
  
    @PostMapping("/revoke")
    @PreAuthorize("hasAuthority('REVOKE_CERTIFICATE')")
    public ResponseEntity<HttpStatus> revokeCertificate(@RequestBody EmailRequestDTO request) {
        _ocspService.revokeCertificate(request.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/revoke")
    @PreAuthorize("hasAuthority('VIEW_REVOKED')")
    public List<OCSPResponse> getRevokedCertificates() throws Exception {
        return _ocspService.getAll();
    }
}
