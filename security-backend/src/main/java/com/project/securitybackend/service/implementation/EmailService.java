package com.project.securitybackend.service.implementation;
import com.project.securitybackend.config.EmailContext;
import com.project.securitybackend.entity.SimpleUser;
import com.project.securitybackend.service.definition.IEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import com.project.securitybackend.security.TokenUtils;
import com.project.securitybackend.service.definition.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private TokenUtils tokenUtils;

    public ResponseEntity<?> sendPasswordRecoveryEmail(String subject, String to, String from, String name) throws MessagingException {
        Context context = new Context();
        context.setVariable("user", name);
        String hash = tokenUtils.generateToken(to);
        context.setVariable("link", "http://localhost:4200/recover-password/" + hash);

        String body = templateEngine.process("passwordRecovery", context);
        sendMail(subject, to, from, body);

        return new ResponseEntity<>("We have sent a email to the account you provided, go check your email", HttpStatus.OK);
    }

    @Override
    public void sendMail(String subject, String to, String from, String body) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(subject);
        helper.setText(body, true);
        helper.setTo("ms.mishoni@gmail.com");//staviti ovde to
        helper.setSentDate(new Date());

        javaMailSender.send(mimeMessage);
    }
  
  private final EmailContext _emailContext;

    public EmailService(EmailContext emailContext) {
        _emailContext = emailContext;
    }

    @Override
    public void confirmationMail(SimpleUser simpleUser) {
        String to = simpleUser.getUsername();
        String subject = "Confirm Account";
        Context context = new Context();
        String link = String.format("http://localhost:4200/account-confirmation/%s", simpleUser.getId());
        context.setVariable("name", String.format("%s %s", simpleUser.getFirstName(), simpleUser.getLastName()));
        context.setVariable("link", String.format("%s", link));
        _emailContext.send(to, subject, "confirmationMail", context);
    }
}
