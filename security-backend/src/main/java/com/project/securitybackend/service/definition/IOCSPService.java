package com.project.securitybackend.service.definition;

import com.project.securitybackend.dto.response.OCSPResponse;
import com.project.securitybackend.entity.OCSPEntity;
import com.project.securitybackend.util.enums.RevocationStatus;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.UUID;

public interface IOCSPService {

    OCSPResponse getOCSPEntity(UUID id);

    OCSPEntity getOCSPEntityBySerialNum(BigInteger serial_num);

    List<OCSPResponse> getAll() throws Exception;

    List<OCSPEntity> getAllByRevoker(UUID id);

    RevocationStatus check(X509Certificate certificate, X509Certificate issuerCert);

    RevocationStatus revoke(X509Certificate certificate, UUID id);

    RevocationStatus activate(X509Certificate certificate, UUID id);

    boolean checkCertificateValidity(X509Certificate certificate);

    boolean revokeCertificate(String email);

    RevocationStatus checkCertificateStatus(BigInteger serialNumber);
}
