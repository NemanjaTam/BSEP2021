package com.project.securitybackend.controller;

import com.project.securitybackend.dto.request.CertificateRequestRequest;
import com.project.securitybackend.dto.request.EmailRequestDTO;
import com.project.securitybackend.dto.request.IssuerEndDateRequest;
import com.project.securitybackend.dto.request.UUIDRequestDTO;
import com.project.securitybackend.dto.response.CertificateRequestResponse;
import com.project.securitybackend.dto.response.IssuerEndDateResponse;
import com.project.securitybackend.dto.response.PossibleExtensionsResponse;
import com.project.securitybackend.service.implementation.CertificateRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/certificate-requests")
public class CertificateRequestController {

    private final CertificateRequestService _certificateRequestService;

    public CertificateRequestController(CertificateRequestService certificateRequestService) {
        _certificateRequestService = certificateRequestService;
    }

    @PostMapping
    public CertificateRequestResponse createCertificateRequest(@RequestBody CertificateRequestRequest request) throws Exception {
        return _certificateRequestService.createCertificateRequest(request);
    }

    @GetMapping
    public List<CertificateRequestResponse> getAllCertificateRequests() throws Exception {
        return _certificateRequestService.getAllCertificateRequests();
    }

    @GetMapping("/{id}/certificate-request")
    public CertificateRequestResponse getCertificateRequest(@PathVariable UUID id) throws Exception {
        return _certificateRequestService.getCertificateRequest(id);
    }

    @PostMapping("/approve")
    public void approveCertificateRequest(@RequestBody CertificateRequestRequest request) throws Exception {
        _certificateRequestService.approveCertificateRequest(request);
    }

    @PostMapping("/deny")
    public void denyCertificateRequest(@RequestBody UUIDRequestDTO request) throws Exception {
        _certificateRequestService.denyCertificateRequest(request.getUuid());
    }

    @PostMapping("/issuer")
    public IssuerEndDateResponse getIssuerEndDate(@RequestBody IssuerEndDateRequest request){
        return _certificateRequestService.getIssuerCertificateEndDate(request);
    }

    @PostMapping("/possible-extensions")
    public PossibleExtensionsResponse getPossibleExtensions(@RequestBody EmailRequestDTO request){
        return _certificateRequestService.getPossibleExtensions(request);
    }
}
