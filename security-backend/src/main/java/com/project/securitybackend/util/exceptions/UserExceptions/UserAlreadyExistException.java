package com.project.securitybackend.util.exceptions.UserExceptions;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException(String username) {
        super(String.format("User with username (%s) already exists.", username));
    }
}
