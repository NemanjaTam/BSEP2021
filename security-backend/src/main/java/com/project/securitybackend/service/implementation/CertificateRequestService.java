package com.project.securitybackend.service.implementation;

import com.project.securitybackend.config.AlgorithmConfig;
import com.project.securitybackend.converter.CertificateConverter;
import com.project.securitybackend.dto.request.CertificateRequestRequest;
import com.project.securitybackend.dto.request.EmailRequestDTO;
import com.project.securitybackend.dto.request.IssuerEndDateRequest;
import com.project.securitybackend.dto.response.CertificateRequestResponse;
import com.project.securitybackend.dto.response.IssuerEndDateResponse;
import com.project.securitybackend.dto.response.PossibleExtensionsResponse;
import com.project.securitybackend.entity.CertificateRequest;
import com.project.securitybackend.entity.CertificatesExtensions;
import com.project.securitybackend.entity.Incrementer;
import com.project.securitybackend.repository.ICertificateRequestRepository;
import com.project.securitybackend.repository.ICertificatesExtensionsRepository;
import com.project.securitybackend.repository.IIncrementerRepository;
import com.project.securitybackend.service.definition.*;
import com.project.securitybackend.util.exceptions.CertificateExceptions.CertificateEmailAlreadyExistException;
import com.project.securitybackend.util.exceptions.CertificateExceptions.EmptyCertificateListException;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "FieldCanBeLocal", "unused"})
@Service
public class CertificateRequestService implements ICertificateRequestService {

    private final AlgorithmConfig config;
    private final ICertificateRequestRepository _certificateRequestRepository;
    private final IKeyStoresReaderService _keyStoresReaderService;
    private final ISignatureService _signatureService;
    private final IKeyStoresWriterService _keyStoreWriterService;
    private final IIncrementerRepository _incrementerRepository;
    private final IKeyStoresWriterService _keyStoresWriterService;
    private final ICertificateService _certificateService;
    private final ICertificatesExtensionsRepository _certificatesExtensionsRepository;

    public CertificateRequestService(AlgorithmConfig config, ICertificateRequestRepository certificateRequestRepository, IKeyStoresReaderService keyStoresReaderService, ISignatureService signatureService, IKeyStoresWriterService keyStoresWriterService, IIncrementerRepository incrementerRepository, IKeyStoresWriterService keyStoresWriterService1, ICertificateService certificateService, ICertificatesExtensionsRepository certificatesExtensionsRepository) {
        this.config = config;
        _certificateRequestRepository = certificateRequestRepository;
        _keyStoresReaderService = keyStoresReaderService;
        _signatureService = signatureService;
        _keyStoreWriterService = keyStoresWriterService;
        _incrementerRepository = incrementerRepository;
        _keyStoresWriterService = keyStoresWriterService1;
        _certificateService = certificateService;
        _certificatesExtensionsRepository = certificatesExtensionsRepository;
    }

    @Override
    public CertificateRequestResponse createCertificateRequest(CertificateRequestRequest request) {
        System.out.println(request);
        if(emailExist(request.getEmail())){
            throw new CertificateEmailAlreadyExistException(request.getEmail());
        }
        CertificateRequest certificateRequest = new CertificateRequest();
        certificateRequest.setCountry(request.getCountry());
        certificateRequest.setFirstName(request.getFirstName());
        certificateRequest.setLastName(request.getLastName());
        certificateRequest.setOrganisation(request.getOrganisation());
        certificateRequest.setOrganisationUnit(request.getOrganisationUnit());
        certificateRequest.setEmail(request.getEmail());
        certificateRequest.setExtension(request.getExtension());
        certificateRequest.setCertificateAuthority(request.isCertificateAuthority());
        certificateRequest.setRoot(request.isRootCert());
        _certificateRequestRepository.save(certificateRequest);

        return mapCertificateRequestToCertificateRequestResponse(certificateRequest);
    }

    private boolean emailExist(String email) {
        List<X509Certificate> endUserCertificates = _certificateService.getAllActiveEndUserCertificates();
        List<X509Certificate> intermediateCertificates = _certificateService.getAllActiveIntermediateCertificates();
        List<X509Certificate> rootCertificates = _certificateService.getAllActiveRootCertificates();
        if(checkExist(endUserCertificates, email)
                || checkExist(intermediateCertificates, email)
                || checkExist(rootCertificates, email)){
            return true;
        }

        CertificateRequest certificateRequests = _certificateRequestRepository.findOneByEmail(email);
        return certificateRequests != null;
    }

    private boolean checkExist(List<X509Certificate> list, String email){
        for (X509Certificate certificate : list) {
            HashMap<String, String> subjectData;
            subjectData = CertificateConverter.getDataFromCertificate(certificate.getSubjectDN().getName());
            if(subjectData.get("email").equals(email)){
                return true;
            }
        }

        return false;
    }

    @Override
    public CertificateRequestResponse getCertificateRequest(UUID id) {
        CertificateRequest certificateRequest = _certificateRequestRepository.findOneById(id);
        return mapCertificateRequestToCertificateRequestResponse(certificateRequest);
    }

    @Override
    public List<CertificateRequestResponse> getAllCertificateRequests() {
        List<CertificateRequest> certificateRequests = _certificateRequestRepository.findAll();

        if(certificateRequests.isEmpty()){
            throw new EmptyCertificateListException();
        }

        return certificateRequests.stream()
                .map(this::mapCertificateRequestToCertificateRequestResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void approveCertificateRequest(CertificateRequestRequest request) throws Exception {
//        System.out.println(request);
        CertificateRequest certificateRequest = _certificateRequestRepository.findOneByEmail(request.getEmail());
        X509Certificate certificate = generateCertificate(request, certificateRequest.getId());
        _certificateService.saveCertificate(certificate, request.getExtension());
        _certificateRequestRepository.delete(certificateRequest);
    }

    @Override
    public void denyCertificateRequest(UUID certificateId) {
        _certificateRequestRepository.deleteById(certificateId);
    }


    private CertificateRequestResponse mapCertificateRequestToCertificateRequestResponse(CertificateRequest request){
        CertificateRequestResponse response = new CertificateRequestResponse();
        response.setId(request.getId());
        response.setCountry(request.getCountry());
        response.setEmail(request.getEmail());
        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setOrganisation(request.getOrganisation());
        response.setOrganisationUnit(request.getOrganisationUnit());
        response.setExtension(request.getExtension());
        response.setRootCert(request.isRoot());
        if(request.isCertificateAuthority()){
            response.setCaOrEnd("Certificate authority");
            return response;
        }

        response.setCaOrEnd("End user");
        return response;
    }

    /**
     * @param email of the issuer
     * @return the end date of issuers certificate
     * */
    private Date getIssuerEndDate(String email){
        X509Certificate issuerCert = this._keyStoresReaderService.readCertificate(config.getRootFileName(), config.getKsPassword(), email);
        if(issuerCert == null){
            issuerCert = this._keyStoresReaderService.readCertificate(config.getCAFileName(), config.getKsPassword(), email);
        }

        return issuerCert.getNotAfter();
    }

    /**
     * @param data the data for the certificate to be passed from request
     * @return the X509Certificate of the subject
     * */
    private X509Certificate generateCertificate(CertificateRequestRequest data, UUID id) throws CertificateException {
        JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
        builder = builder.setProvider("BC");

        //Citam ko mu je izdavac sert
        X509Certificate issuerCert = this._keyStoresReaderService.readCertificate(config.getRootFileName(), config.getKsPassword(), data.getIssuerEmail());
        if(issuerCert == null){
            issuerCert = this._keyStoresReaderService.readCertificate(config.getCAFileName(), config.getKsPassword(), data.getIssuerEmail());
        }

//         if(issuerCert.getNotAfter() < data.getRequestDate()){
//            return null;
//         }

        KeyPair keyPair = this._signatureService.generateKeys();

        //Citam privatni kljuc izdavacu
        PrivateKey privKey = this._keyStoresReaderService.readPrivateKey(config.getRootFileName(), config.getKsPassword(), data.getIssuerEmail(), "admin");
        if(privKey == null){
            privKey = this._keyStoresReaderService.readPrivateKey(config.getCAFileName(),config.getKsPassword(), data.getIssuerEmail(), "admin");
        }

        //Za potpisivanje hesiranog sert
        ContentSigner contentSigner = null;
        try {
            contentSigner = builder.build(privKey);
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        }

        CertificatesExtensions certificatesExtensions = new CertificatesExtensions();
        certificatesExtensions.setEmail(data.getEmail());
        certificatesExtensions.setDigitalSignature(data.isDigitalSignature());
        certificatesExtensions.setKeyAgreement(data.isKeyAgreement());
        certificatesExtensions.setKeyEncipherment(data.isKeyEncipherment());
        certificatesExtensions.setNonRepudiation(data.isNonRepudiation());
        _certificatesExtensionsRepository.save(certificatesExtensions);

        //Pravljenje identiteta na sertifikatu
        X500Name x500Name = this.getX500Name(data, id);
        Incrementer incrementer = _incrementerRepository.findAll().get(0);

        //build-ovanje sertifikata
        X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(new JcaX509CertificateHolder(issuerCert).getSubject(),
                new BigInteger(incrementer.getInc().toString()),
                new Date(),
                data.getEndDate(),
                x500Name,
                keyPair.getPublic());

        int newInc = incrementer.getInc() + 1;
        incrementer.setInc(newInc);
        _incrementerRepository.save(incrementer);

        //Dodavanje ekstenzije da je CA ukoliko je to uneto u formi
        if(data.isCertificateAuthority() || data.isRootCert()){

            KeyUsage usage = new KeyUsage(KeyUsage.keyCertSign
                    | (data.isDigitalSignature() ? KeyUsage.digitalSignature : KeyUsage.keyCertSign )
                    | (data.isNonRepudiation() ? KeyUsage.nonRepudiation : KeyUsage.keyCertSign  )
                    | (data.isKeyAgreement() ? KeyUsage.keyAgreement : KeyUsage.keyCertSign )
                    | (data.isKeyEncipherment() ? KeyUsage.keyEncipherment : KeyUsage.keyCertSign ));

            try {
                certGen.addExtension(X509Extensions.BasicConstraints, true,
                        new BasicConstraints(true));
                certGen.addExtension(Extension.keyUsage, true, usage);
            } catch (CertIOException e) {
                e.printStackTrace();
            }
        } else {
            if(data.isDigitalSignature()){
                KeyUsage usage = new KeyUsage(KeyUsage.digitalSignature
                        | (data.isNonRepudiation() ? KeyUsage.nonRepudiation : KeyUsage.digitalSignature  )
                        | (data.isKeyAgreement() ? KeyUsage.keyAgreement : KeyUsage.digitalSignature )
                        | (data.isKeyEncipherment() ? KeyUsage.keyEncipherment : KeyUsage.digitalSignature ));

                try {
                    certGen.addExtension(Extension.keyUsage, true, usage);
                } catch (CertIOException e) {
                    e.printStackTrace();
                }
            }else if(data.isNonRepudiation()){
                KeyUsage usage = new KeyUsage(KeyUsage.nonRepudiation
                        | (data.isKeyAgreement() ? KeyUsage.keyAgreement : KeyUsage.nonRepudiation )
                        | (data.isKeyEncipherment() ? KeyUsage.keyEncipherment : KeyUsage.nonRepudiation ));

                try {
                    certGen.addExtension(Extension.keyUsage, true, usage);
                } catch (CertIOException e) {
                    e.printStackTrace();
                }
            }else if(data.isKeyAgreement()){
                KeyUsage usage = new KeyUsage(KeyUsage.keyAgreement
                        | (data.isKeyEncipherment() ? KeyUsage.keyEncipherment : KeyUsage.keyAgreement ));

                try {
                    certGen.addExtension(Extension.keyUsage, true, usage);
                } catch (CertIOException e) {
                    e.printStackTrace();
                }
            }else if(data.isKeyEncipherment()){
                KeyUsage usage = new KeyUsage(KeyUsage.keyEncipherment);

                try {
                    certGen.addExtension(Extension.keyUsage, true, usage);
                } catch (CertIOException e) {
                    e.printStackTrace();
                }
            }
        }

        X509CertificateHolder certHolder = certGen.build(contentSigner);

        JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
        certConverter = certConverter.setProvider("BC");

        if(data.isRootCert()) {
            _keyStoreWriterService.write(data.getEmail(), keyPair.getPrivate(), config.getRootFileName(), config.getKsPassword(), certConverter.getCertificate(certHolder));
        }
        else if(data.isCertificateAuthority()){
            _keyStoreWriterService.write(data.getEmail(), keyPair.getPrivate(), config.getCAFileName(), config.getKsPassword(), certConverter.getCertificate(certHolder));
        }else {
            _keyStoreWriterService.write(data.getEmail(), keyPair.getPrivate(), config.getEnd_userFileName(), config.getKsPassword(), certConverter.getCertificate(certHolder));
        }

        return certConverter.getCertificate(certHolder);
    }

    private X500Name getX500Name(CertificateRequestRequest data, UUID id){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        String cn = data.getFirstName() + ' ' + data.getLastName();
        builder.addRDN(BCStyle.CN, cn);
        builder.addRDN(BCStyle.SURNAME, data.getLastName());
        builder.addRDN(BCStyle.GIVENNAME, data.getFirstName());
        builder.addRDN(BCStyle.O, data.getOrganisation());
        builder.addRDN(BCStyle.OU, data.getOrganisationUnit());
        builder.addRDN(BCStyle.C, data.getCountry());
        builder.addRDN(BCStyle.E, data.getEmail());
        //UID (USER ID) je ID korisnika
        builder.addRDN(BCStyle.UID, String.valueOf(id));
        return builder.build();
    }

    @Override
    public IssuerEndDateResponse getIssuerCertificateEndDate(IssuerEndDateRequest request) {

        X509Certificate certificate = this._keyStoresReaderService.readCertificate(config.getRootFileName(), config.getKsPassword(), request.getEmail());
        if(certificate == null){
            certificate = this._keyStoresReaderService.readCertificate(config.getCAFileName(), config.getKsPassword(), request.getEmail());
        }

        return new IssuerEndDateResponse(certificate.getNotAfter());
    }

    @Override
    public PossibleExtensionsResponse getPossibleExtensions(EmailRequestDTO request) {
        CertificatesExtensions certificatesExtensions = _certificatesExtensionsRepository.findOneByEmail(request.getEmail());
        PossibleExtensionsResponse response = new PossibleExtensionsResponse();
        if(certificatesExtensions == null){
            response.setDigitalSignature(true);
            response.setKeyAgreement(true);
            response.setKeyEncipherment(true);
            response.setNonRepudiation(true);
            return response;
        }
        response.setDigitalSignature(certificatesExtensions.isDigitalSignature());
        response.setKeyAgreement(certificatesExtensions.isKeyAgreement());
        response.setKeyEncipherment(certificatesExtensions.isKeyEncipherment());
        response.setNonRepudiation(certificatesExtensions.isNonRepudiation());
        return response;
    }
}
