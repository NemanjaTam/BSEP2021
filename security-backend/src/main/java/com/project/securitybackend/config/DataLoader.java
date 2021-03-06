package com.project.securitybackend.config;

import com.project.securitybackend.service.implementation.KeyStoresWriterService;
import com.project.securitybackend.entity.Admin;
import com.project.securitybackend.repository.IAdminRepository;
import com.project.securitybackend.service.implementation.SignatureService;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class DataLoader implements ApplicationRunner {

    private final KeyStoresWriterService keyStoresWriterService;
    private final SignatureService signatureService;

    private final PasswordEncoder _passwordEncoder;

    private final IAdminRepository _adminRepository;

    public DataLoader(KeyStoresWriterService keyStoresWriterService, SignatureService signatureService, PasswordEncoder passwordEncoder, IAdminRepository adminRepository) {
        this.keyStoresWriterService = keyStoresWriterService;
        this.signatureService = signatureService;
        _passwordEncoder = passwordEncoder;
        _adminRepository = adminRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        KeyPair keyPair = signatureService.generateKeys();
        X509Certificate cert = this.createInitialRootCertificate(keyPair);
        keyStoresWriterService.write("goran.sladic@uns.ac.rs", keyPair.getPrivate(), "keystoreRoot.jks", "admin", cert);

//        Admin admin = _adminRepository.findOneById(UUID.fromString("e47ca3f0-4906-495f-b508-4d9af7013575"));
//        admin.setPassword(_passwordEncoder.encode(admin.getPassword()));
//        _adminRepository.save(admin);
    }

    private X509Certificate createInitialRootCertificate(KeyPair keyPair){
        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        builder = builder.setProvider("BC");

        ContentSigner contentSigner = null;
        try {
            contentSigner = builder.build(keyPair.getPrivate());
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }

        X500Name x500Name = this.getX500Name();

        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, 10);

        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(x500Name,
                new BigInteger("10"),
                new Date(),
                c.getTime(),
                x500Name,
                keyPair.getPublic());
        try {
            certGen.addExtension(X509Extensions.BasicConstraints, true,
                    new BasicConstraints(true));
        } catch (CertIOException e) {
            e.printStackTrace();
        }

        X509CertificateHolder certHolder = certGen.build(contentSigner);

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");

        try {
            return certConverter.getCertificate(certHolder);
        } catch (CertificateException e) {
            e.printStackTrace();
        }

        return null;
    }

    private X500Name getX500Name(){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        String cn = "Goran Sladic";
        builder.addRDN(BCStyle.CN, cn);
        builder.addRDN(BCStyle.SURNAME, "Sladic");
        builder.addRDN(BCStyle.GIVENNAME, "Goran");
        builder.addRDN(BCStyle.O, "FTN");
        builder.addRDN(BCStyle.OU, "UNS");
        builder.addRDN(BCStyle.C, "RS");
        builder.addRDN(BCStyle.E, "goran.sladic@uns.ac.rs");
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, String.valueOf("a5af6079-5511-4eec-845e-c8b62539978d"));
        return builder.build();
    }
}
