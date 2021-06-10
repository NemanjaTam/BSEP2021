package com.project.securitybackend.controller;

import com.project.securitybackend.dto.request.EmailRequestDTO;
import com.project.securitybackend.dto.request.LoginRequest;
import com.project.securitybackend.dto.request.RecoverPasswordRequest;
import com.project.securitybackend.dto.request.RegistrationRequest;
import com.project.securitybackend.dto.response.RegistrationResponse;
import com.project.securitybackend.dto.response.UserResponse;
import com.project.securitybackend.service.definition.IAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService _authService;

    public AuthController(IAuthService loginService) {
        _authService = loginService;
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest request) {
        return _authService.login(request);
    }

    @PostMapping("/registration")
    public RegistrationResponse registration(@Valid @RequestBody RegistrationRequest request) {
        return _authService.registration(request);
    }
    
    @PutMapping("/password-recovery")
    public ResponseEntity<?> recoverPassword(@RequestBody RecoverPasswordRequest request){
        return _authService.recoverPassword(request);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody EmailRequestDTO request) throws MessagingException {
        return _authService.forgotPassword(request);
    }
}
