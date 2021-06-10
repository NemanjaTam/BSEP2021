package com.project.securitybackend.service.definition;

import com.project.securitybackend.dto.request.EmailRequestDTO;
import com.project.securitybackend.dto.request.LoginRequest;
import com.project.securitybackend.dto.request.RecoverPasswordRequest;
import com.project.securitybackend.dto.request.RegistrationRequest;
import com.project.securitybackend.dto.response.RegistrationResponse;
import com.project.securitybackend.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface IAuthService {

    UserResponse login(LoginRequest request);

    RegistrationResponse registration(RegistrationRequest request);

    ResponseEntity<?> recoverPassword(RecoverPasswordRequest request);

    ResponseEntity<?> forgotPassword(EmailRequestDTO request) throws MessagingException;
}
