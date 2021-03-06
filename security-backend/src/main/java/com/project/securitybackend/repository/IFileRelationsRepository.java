package com.project.securitybackend.repository;

import com.project.securitybackend.entity.FileRelations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IFileRelationsRepository extends JpaRepository<FileRelations, UUID> {

    FileRelations findOneByEmail(String email);
}