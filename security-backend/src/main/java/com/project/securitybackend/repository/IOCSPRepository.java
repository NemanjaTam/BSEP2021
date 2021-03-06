package com.project.securitybackend.repository;

import com.project.securitybackend.entity.OCSPEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

public interface IOCSPRepository extends JpaRepository<OCSPEntity, UUID> {

    OCSPEntity findOneById(UUID id);
    OCSPEntity findOneBySerialNum(BigInteger serial_num);
    List<OCSPEntity> findAllByRevoker(UUID id);
}
