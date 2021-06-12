package com.project.securitybackend.controller;

import com.project.securitybackend.dto.response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class HttpsTestController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<?> httpsTestMethod(){
        ResponseEntity<ResponseDTO> response = restTemplate.getForEntity("https://localhost:9001/api", ResponseDTO.class);
        if(!response.getStatusCode().equals(HttpStatus.OK)){
            System.out.println("Nije uspelo");
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseDTO(response.getBody().getMessage()), HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<?> dummyApiEndpoint(){
        return new ResponseEntity<>(new ResponseDTO("Response from BSEP application over https"), HttpStatus.OK);
    }
}
