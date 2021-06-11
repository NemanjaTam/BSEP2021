package com.project.securitybackend.util.exceptions.CertificateExceptions;

public class NoRevokedCertificatesException extends RuntimeException {

    public NoRevokedCertificatesException() {
        super("There are no revoked certificates");
    }
}