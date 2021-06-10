package com.project.securitybackend.service.definition;

public interface IHashService {

    byte[] generateSalt();

    byte[] hash(String hashEntity, byte[] salt);

}
