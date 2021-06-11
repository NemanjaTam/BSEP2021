package com.project.securitybackend.service.implementation;

import com.project.securitybackend.dto.response.SimpleUserResponse;
import com.project.securitybackend.entity.SimpleUser;
import com.project.securitybackend.repository.ISimpleUserRepository;
import com.project.securitybackend.service.definition.IEmailService;
import com.project.securitybackend.service.definition.ISimpleUserService;
import com.project.securitybackend.util.enums.UserStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SimpleUserService implements ISimpleUserService {

    private final ISimpleUserRepository _simpleUserRepository;
    private final IEmailService _emailService;

    public SimpleUserService(ISimpleUserRepository simpleUserRepository, IEmailService emailService) {
        _simpleUserRepository = simpleUserRepository;
        _emailService = emailService;
    }

    @Override
    public List<SimpleUserResponse> getUsersByUserStatus(UserStatus userStatus) {
        List<SimpleUser> simpleUsersFilteredByStatus = _simpleUserRepository.findByUserStatus(userStatus);
        return mapSimpleUsersToSimpleUsersResponse(simpleUsersFilteredByStatus);
    }

    @Override
    public void approveRegistrationRequest(UUID userId) {
        SimpleUser simpleUser = _simpleUserRepository.findById(userId);
        simpleUser.setUserStatus(UserStatus.APPROVED);
        _emailService.confirmationMail(simpleUser);
        _simpleUserRepository.save(simpleUser);
    }

    @Override
    public void denyRegistrationRequest(UUID userId) {
        SimpleUser simpleUser = _simpleUserRepository.findById(userId);
        simpleUser.setUserStatus(UserStatus.DENIED);
        _simpleUserRepository.save(simpleUser);
    }

    @Override
    public void confirmAccount(UUID userId) {
        SimpleUser simpleUser = _simpleUserRepository.findById(userId);
        simpleUser.setUserConfirmAccount(true);
        _simpleUserRepository.save(simpleUser);
    }

    private List<SimpleUserResponse> mapSimpleUsersToSimpleUsersResponse(List<SimpleUser> simpleUsers) {
        List<SimpleUserResponse> retList = new ArrayList<>();
        for (SimpleUser simpleUser : simpleUsers) {
            retList.add(mapSimpleUserToSimpleUserResponse(simpleUser));
        }

        return retList;
    }

    private SimpleUserResponse mapSimpleUserToSimpleUserResponse(SimpleUser simpleUser) {
        SimpleUserResponse retSimpleUser = new SimpleUserResponse();
        retSimpleUser.setId(simpleUser.getId());
        retSimpleUser.setUsername(simpleUser.getUsername());
        retSimpleUser.setFirstName(simpleUser.getFirstName());
        retSimpleUser.setLastName(simpleUser.getLastName());
        retSimpleUser.setConfirmationDate(formatDate(simpleUser.getConfirmationTime()));

        return retSimpleUser;
    }

    private String formatDate(LocalDateTime confirmationTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return confirmationTime.format(formatter);
    }
}

