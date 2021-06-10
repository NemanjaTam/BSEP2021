package com.project.securitybackend.service.implementation;

import com.project.securitybackend.entity.Admin;
import com.project.securitybackend.repository.IAdminRepository;
import com.project.securitybackend.service.definition.IAdminService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdminService implements IAdminService {

    private final IAdminRepository _adminRepository;

    public AdminService(IAdminRepository adminRepository) {
        _adminRepository = adminRepository;
    }

    @Override
    public Admin findOneById(UUID id) {
        return _adminRepository.findOneById(id);
    }

    @Override
    public List<Admin> findAll() {
        return _adminRepository.findAll();
    }
}
