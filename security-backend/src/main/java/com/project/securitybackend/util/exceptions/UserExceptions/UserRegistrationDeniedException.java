package com.project.securitybackend.util.exceptions.UserExceptions;

public class UserRegistrationDeniedException extends RuntimeException {

    public UserRegistrationDeniedException() {
        super("Your registration has been denied.");
    }
}