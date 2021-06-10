package com.project.securitybackend.repository;

import com.project.securitybackend.entity.Incrementer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IIncrementerRepository extends JpaRepository<Incrementer, UUID> {
}
