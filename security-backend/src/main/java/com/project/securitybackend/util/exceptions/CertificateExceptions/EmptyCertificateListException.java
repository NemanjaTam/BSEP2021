package com.project.securitybackend.util.exceptions.CertificateExceptions;

public class EmptyCertificateListException extends RuntimeException {

    public EmptyCertificateListException() {
        super("There are no certificate requests.");
    }
}