package com.project.securitybackend.repository;

import com.project.securitybackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

    User findOneById(UUID id);

    User findOneByUsername(String username);

    List<User> findAllByDeleted(boolean deleted);

    @Query(value = "select * from user_entity u where u.username = :username and u.password = :password", nativeQuery = true)
    User findOneByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}