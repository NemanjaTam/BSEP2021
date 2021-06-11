package com.project.securitybackend.repository;

import com.project.securitybackend.entity.SimpleUser;
import com.project.securitybackend.util.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ISimpleUserRepository extends JpaRepository<SimpleUser, Long> {

    List<SimpleUser> findByUserStatus(UserStatus userStatus);

    SimpleUser findById(UUID id);
}
