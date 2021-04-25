package tim25.BSEP_PROJECT.dto;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.util.Date;

public class CertificateDTO {

    private String commonName;

    private String email;

    private String country;

    private String organisation;

    private String organisationUnit;

    private String issuerEmail;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private Date endDate;

    //dodatne ekstenzije

    private boolean digitalSignature;

    private boolean nonRepudiation;

    private boolean keyAgreement;

    private boolean keyEncipherment;

}