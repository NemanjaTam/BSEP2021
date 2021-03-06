package com.project.securitybackend.service.definition;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface IKeyStoresWriterService {

    void write(String alias, PrivateKey privateKey, String fileName, String password, Certificate certificate);

}
