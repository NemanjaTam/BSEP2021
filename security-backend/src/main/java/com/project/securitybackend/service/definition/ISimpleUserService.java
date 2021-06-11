package com.project.securitybackend.service.definition;

import com.project.securitybackend.dto.response.SimpleUserResponse;
import com.project.securitybackend.util.enums.UserStatus;

import java.util.List;
import java.util.UUID;

public interface ISimpleUserService {

    List<SimpleUserResponse> getUsersByUserStatus(UserStatus userStatus);

    void approveRegistrationRequest(UUID userId);

    void denyRegistrationRequest(UUID userId);

    void confirmAccount(UUID userId);
}
