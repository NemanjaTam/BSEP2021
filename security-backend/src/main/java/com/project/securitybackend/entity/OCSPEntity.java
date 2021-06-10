package com.project.securitybackend.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigInteger;
import java.util.UUID;

@SuppressWarnings({"unused", "JpaDataSourceORMInspection"})
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OCSPEntity extends BaseEntity {

    @Column(name = "serial_num")
    private BigInteger serialNum;

    private UUID revoker;

    private String subject;

    private String issuer;
}
