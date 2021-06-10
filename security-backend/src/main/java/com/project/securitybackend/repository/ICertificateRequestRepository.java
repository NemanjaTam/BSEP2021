package com.project.securitybackend.repository;

import com.project.securitybackend.entity.CertificateRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICertificateRequestRepository extends JpaRepository<CertificateRequest, UUID> {

    CertificateRequest findOneById(UUID id);

    CertificateRequest findOneByEmail(String email);
}
