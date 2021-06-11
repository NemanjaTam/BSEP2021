package com.project.securitybackend.util.exceptions.CertificateExceptions;

public class NoValidCertificatesException extends RuntimeException {

    public NoValidCertificatesException(String certificateType) {
        super(String.format("There are no valid %s certificates", certificateType));
    }
}
