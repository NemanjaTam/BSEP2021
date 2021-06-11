package com.project.securitybackend.util.exceptions.UserExceptions;


public class UserRegistrationNotApprovedException extends RuntimeException {

    public UserRegistrationNotApprovedException() {
        super("Your registration hasn't been approved yet.");
    }
}