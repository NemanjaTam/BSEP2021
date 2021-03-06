package com.project.securitybackend.controller;

import com.project.securitybackend.service.implementation.OCSPService;
import com.project.securitybackend.util.enums.RevocationStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@SuppressWarnings({"unused", "SpellCheckingInspection"})
@RestController
@RequestMapping("/ocsp")
public class OCSPController {

    private final OCSPService _ocspService;

    public OCSPController(OCSPService ocspService) {
        _ocspService = ocspService;
    }

    @GetMapping("/{serial_number}/check")
    public RevocationStatus getRevokedCertificates(@PathVariable("serial_number") String serialNumber) throws NumberFormatException {
        BigInteger serialNum;
        try {
             serialNum = new BigInteger(serialNumber);
        }catch (NumberFormatException e){
            throw new NumberFormatException("Number format is not valid.");
        }
        return _ocspService.checkCertificateStatus(serialNum);
    }

}
