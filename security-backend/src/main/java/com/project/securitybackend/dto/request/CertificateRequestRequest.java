package com.project.securitybackend.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@SuppressWarnings("SpellCheckingInspection")
@Data
public class CertificateRequestRequest {

    private String firstName;

    private String lastName;

    private String email;

    private String country;

    private String organisation;

    private String organisationUnit;

    private String extension;

    private boolean certificateAuthority;

    private boolean rootCert;

    private String issuerEmail;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date endDate;

//    dodatne ekstenzije

//    sluzi za digitalni potpis
    private boolean digitalSignature;

//    koristi se kada se javni kljuc koristi za verifikaciju digitalnih potpisa koji se koriste za pruzanje usluge ne potvrdjivanja
    private boolean nonRepudiation;

//    koristi se kada posiljalac i primalac javnog kljuca moraju da dobiju kljuc bez sifrovanja, a zatim se moze koristiti za sifrovanje
//    poruka izmedju posiljaoca i primaoca
    private boolean keyAgreement;

//    koristi se kada ce se sertifikat koristiti sa protokolom koji sifrira kljuceve
    private boolean keyEncipherment;

    //signingCertificate se podrazumeva kada je CA - koristiti kada se javni kljuc subjekta koristi za verifikaciju potpisa na sertifikatima

}
