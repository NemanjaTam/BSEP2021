package com.bsep.application.controller;

import com.bsep.application.dto.response.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/api")
public class DummyController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<?> dummyApiEndpoint(){
        return new ResponseEntity<>(new ResponseDTO("Response from dummy application over https"), HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<?> dummyRequest(){
        ResponseEntity<ResponseDTO> response = restTemplate.getForEntity("https://localhost:9000/api/test",  ResponseDTO.class);
        if(!response.getStatusCode().equals(HttpStatus.OK)){
            System.out.println("Nije uspelo");
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new ResponseDTO(response.getBody().getMessage()), HttpStatus.OK);
    }

}
