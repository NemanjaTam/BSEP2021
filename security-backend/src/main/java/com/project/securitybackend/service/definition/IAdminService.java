package com.project.securitybackend.service.definition;

import com.project.securitybackend.entity.Admin;

import java.util.List;
import java.util.UUID;

public interface IAdminService {

    Admin findOneById(UUID id);

    List<Admin> findAll();

}
