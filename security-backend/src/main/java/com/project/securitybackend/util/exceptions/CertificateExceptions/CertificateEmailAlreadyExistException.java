package com.project.securitybackend.util.exceptions.CertificateExceptions;

public class CertificateEmailAlreadyExistException extends RuntimeException {

    public CertificateEmailAlreadyExistException(String email) {
        super(String.format("Certificate with Email %s already exists", email));
    }
}