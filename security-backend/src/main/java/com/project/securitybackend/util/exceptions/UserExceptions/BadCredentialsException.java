package com.project.securitybackend.util.exceptions.UserExceptions;

import java.util.UUID;

public class BadCredentialsException extends RuntimeException {

    public BadCredentialsException() {
        super("Bad credentials");
    }
}