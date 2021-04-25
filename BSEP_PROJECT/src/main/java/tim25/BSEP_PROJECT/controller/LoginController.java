package tim25.BSEP_PROJECT.controller;


import tim25.BSEP_PROJECT.dto.RegistrationDTO;

import tim25.BSEP_PROJECT.model.User;
import tim25.BSEP_PROJECT.model.UserTokenState;
import tim25.BSEP_PROJECT.service.LoginService;
import tim25.BSEP_PROJECT.security.TokenUtils;
import tim25.BSEP_PROJECT.security.auth.JwtAuthenticationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import tim25.BSEP_PROJECT.service.UserService;



import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class LoginController {
    @Autowired
    private LoginService service;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserTokenState> loginUser(@RequestBody JwtAuthenticationRequest authenticationRequest,
                                                    HttpServletResponse response) throws AuthenticationException, IOException {
        User log = service.loginUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        if (log != null) {
            final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
            // Ubaci username + password u kontext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user.getUsername());
            int expiresIn = tokenUtils.getExpiredIn();
            tim25.BSEP_PROJECT.model.Authority a = (tim25.BSEP_PROJECT.model.Authority) user.getAuthorities().toArray()[0];
            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, a.getName()));
        } else {
            return new ResponseEntity<UserTokenState>(HttpStatus.NOT_FOUND);
        }

    }


    @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> loginUser(@RequestBody RegistrationDTO registrationDTO,
                                          HttpServletResponse response) throws AuthenticationException, IOException {

        Boolean success = service.registerUser(registrationDTO);
        if (success)
        {
            return ResponseEntity.ok(success);
        }
        else {
            return new ResponseEntity<Boolean>(success, HttpStatus.BAD_REQUEST);
        }

    }

}
