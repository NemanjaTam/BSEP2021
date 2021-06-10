package com.project.securitybackend.repository;

import com.project.securitybackend.entity.CertificatesExtensions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICertificatesExtensionsRepository extends JpaRepository<CertificatesExtensions, UUID> {

    CertificatesExtensions findOneById(UUID id);

    CertificatesExtensions findOneByEmail(String email);
}
