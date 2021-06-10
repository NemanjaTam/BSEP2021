package com.project.securitybackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CertificateRequest extends BaseEntity {

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String email;

    private String country;

    private Date startAt;

    private Date endAt;

    private String organisation;

    private String organisationUnit;

    private String extension;

    private boolean isCertificateAuthority;

    private boolean isRoot;

    private String issuerEmail;
}
