package com.project.securitybackend.service.definition;

import com.project.securitybackend.dto.request.EmailRequestDTO;
import com.project.securitybackend.dto.response.CertificateResponseDTO;
import com.project.securitybackend.util.enums.CertificateType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.List;

public interface ICertificateService {

    List<X509Certificate> getAllActiveEndUserCertificates();

    List<X509Certificate> getAllActiveIntermediateCertificates();

    List<X509Certificate> getAllActiveRootCertificates();

    List<CertificateResponseDTO> listToDTO(CertificateType certificateType, List<X509Certificate> certificateList);

    void saveCertificate(X509Certificate certificate, String extension) throws IOException;

    List<String> getFileName(String email);

    ResponseEntity<Object> downloadCertificate(EmailRequestDTO request);
}
