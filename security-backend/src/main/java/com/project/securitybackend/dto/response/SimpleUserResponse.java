package com.project.securitybackend.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class SimpleUserResponse {

    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private String confirmationDate;
}
