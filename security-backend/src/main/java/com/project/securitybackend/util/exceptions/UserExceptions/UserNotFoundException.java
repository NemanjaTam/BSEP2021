package com.project.securitybackend.util.exceptions.UserExceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("User not found");
    }
}
