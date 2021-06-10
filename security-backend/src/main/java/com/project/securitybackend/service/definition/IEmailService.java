package com.project.securitybackend.service.definition;

import com.project.securitybackend.entity.SimpleUser;
import org.springframework.http.ResponseEntity;
import javax.mail.MessagingException;

public interface IEmailService {

    ResponseEntity<?> sendPasswordRecoveryEmail(String subject, String to, String from, String name) throws MessagingException;

    void sendMail(String subject, String to, String from, String body) throws MessagingException;
  
    void confirmationMail(SimpleUser simpleUser);
}
