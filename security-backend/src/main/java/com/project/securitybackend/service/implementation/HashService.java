package com.project.securitybackend.service.implementation;

import com.project.securitybackend.service.definition.IHashService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

@Service
public class HashService implements IHashService {

    @Override
    public byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    @Override
    public byte[] hash(String hashEntity, byte[] salt) {
        SecretKeyFactory factory;
        try {
            KeySpec spec = new PBEKeySpec(hashEntity.toCharArray(), salt, 65536, 128);
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }

        return null;
    }
}
