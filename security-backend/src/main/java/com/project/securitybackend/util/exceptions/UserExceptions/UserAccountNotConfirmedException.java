package com.project.securitybackend.util.exceptions.UserExceptions;

public class UserAccountNotConfirmedException extends RuntimeException {

    public UserAccountNotConfirmedException() {
        super("You have not approved your account yet. Please check your e-mail.");
    }
}
