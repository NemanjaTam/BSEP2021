package com.project.securitybackend.service.implementation;

import com.project.securitybackend.config.AlgorithmConfig;
import com.project.securitybackend.converter.CertificateConverter;
import com.project.securitybackend.dto.request.EmailRequestDTO;
import com.project.securitybackend.dto.response.CertificateResponseDTO;
import com.project.securitybackend.entity.FileRelations;
import com.project.securitybackend.repository.IFileRelationsRepository;
import com.project.securitybackend.service.definition.ICertificateService;
import com.project.securitybackend.service.definition.IOCSPService;
import com.project.securitybackend.util.enums.CertificateType;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unused", "SpellCheckingInspection", "UnnecessaryLocalVariable", "RedundantStringFormatCall"})
@Service
public class CertificateService implements ICertificateService {

    private final AlgorithmConfig config;
    private final KeyStoresReaderService _keyStoresReaderService;
    private final IFileRelationsRepository _fileRelationsRepository;

    @Autowired
    IOCSPService _ocspListService;

    public CertificateService(AlgorithmConfig config, KeyStoresReaderService keyStoresReaderService, IFileRelationsRepository fileRelationsRepository){
        this.config = config;
        _keyStoresReaderService = keyStoresReaderService;
        _fileRelationsRepository = fileRelationsRepository;
    }

    @Override
    public List<X509Certificate> getAllActiveEndUserCertificates() {
        List<X509Certificate> retList = new ArrayList<>();
        List<X509Certificate> certificates = _keyStoresReaderService.readAllCertificate(config.getEnd_userFileName(), config.getKsPassword());
        for (X509Certificate certificate : certificates) {
            if (_ocspListService.checkCertificateValidity(certificate)) {
                retList.add(certificate);
            }
        }

        return retList;
    }

    @Override
    public List<X509Certificate> getAllActiveIntermediateCertificates() {
        List<X509Certificate> retList = new ArrayList<>();
        System.out.println(config.getCAFileName()+config.getKsPassword());
        List<X509Certificate> CACertificates = _keyStoresReaderService.readAllCertificate(config.getCAFileName(), config.getKsPassword());
        for (X509Certificate certificate : CACertificates) {
            if (_ocspListService.checkCertificateValidity(certificate)) {
                retList.add(certificate);
            }
        }

        return retList;
    }

    @Override
    public List<X509Certificate> getAllActiveRootCertificates() {
        List<X509Certificate> retList = new ArrayList<>();
        List<X509Certificate> RootCertificates = _keyStoresReaderService.readAllCertificate(config.getRootFileName(), config.getKsPassword());
        for (X509Certificate certificate : RootCertificates) {
            if (_ocspListService.checkCertificateValidity(certificate)) {
                retList.add(certificate);
            }
        }

        return retList;
    }

    @Override
    public List<CertificateResponseDTO> listToDTO(CertificateType certificateType, List<X509Certificate> certificateList) {
        List<CertificateResponseDTO> retList = new ArrayList<>();
        for (X509Certificate certificate : certificateList) {
            CertificateResponseDTO certificateResponseDTO = CertificateConverter.toCertificateResponseDTO(certificate);
            certificateResponseDTO.setCertificateType(certificateType);
            retList.add(certificateResponseDTO);
        }

        return retList;
    }

    public ResponseEntity<Object> downloadCertificate(EmailRequestDTO request){
        FileRelations fr = _fileRelationsRepository.findOneByEmail(request.getEmail());
        String fileName = "certificates\\" + fr.getFileName();

        File file = new File(fileName);
        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(file.getName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename="+file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
        return responseEntity;
    }

    public void saveCertificate(X509Certificate certificate, String extension) throws IOException {
        String fileName = certificate.getSubjectDN().getName().split(",")[0].split("=")[1].trim();
        String fn = fileName + extension;

        fileName = "certificates\\" + fileName + extension;

        StringWriter sw = new StringWriter();
        try ( JcaPEMWriter pw = new JcaPEMWriter( sw ) )
        {
            pw.writeObject(certificate);
        }

        FileWriter fw = new FileWriter(fileName);
        fw.write(sw.toString());
        fw.close();

        FileRelations fr = new FileRelations(certificate.getSubjectDN().getName().split(",")[6].split("=")[1], fn);
        _fileRelationsRepository.save(fr);
    }

    public List<String> getFileName(String email){
        FileRelations file = _fileRelationsRepository.findOneByEmail(email);
        List<String> retVal = new ArrayList<String>();
        retVal.add(file.getFileName());
        return retVal;
    }
}
