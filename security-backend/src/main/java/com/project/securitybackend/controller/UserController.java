package com.project.securitybackend.controller;

import com.project.securitybackend.dto.response.SimpleUserResponse;
import com.project.securitybackend.service.definition.ISimpleUserService;
import com.project.securitybackend.util.enums.UserStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final ISimpleUserService _simpleUserService;

    public UserController(ISimpleUserService simpleUserService) {
        _simpleUserService = simpleUserService;
    }

    @GetMapping("/{userStatus}")
    public List<SimpleUserResponse> getUsersByStatus(@PathVariable("userStatus") UserStatus userStatus) {
        return _simpleUserService.getUsersByUserStatus(userStatus);
    }

    @GetMapping("/{id}/approve")
    public void approveUser(@PathVariable("id") UUID userId) {
        _simpleUserService.approveRegistrationRequest(userId);
    }

    @GetMapping("/{id}/deny")
    public void denyUser(@PathVariable("id") UUID userId) {
        _simpleUserService.denyRegistrationRequest(userId);
    }

    @GetMapping("/{id}/confirm")
    public void confirmAccount(@PathVariable("id") UUID userId) {
        _simpleUserService.confirmAccount(userId);
    }
}
