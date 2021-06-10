package com.project.securitybackend.service.implementation;

import com.project.securitybackend.dto.request.EmailRequestDTO;
import com.project.securitybackend.dto.request.LoginRequest;
import com.project.securitybackend.dto.request.RecoverPasswordRequest;
import com.project.securitybackend.dto.request.RegistrationRequest;
import com.project.securitybackend.dto.response.RegistrationResponse;
import com.project.securitybackend.dto.response.UserResponse;
import com.project.securitybackend.entity.Authority;
import com.project.securitybackend.entity.SimpleUser;
import com.project.securitybackend.entity.User;
import com.project.securitybackend.entity.UserDetailsImpl;
import com.project.securitybackend.repository.IAuthorityRepository;
import com.project.securitybackend.repository.ISimpleUserRepository;
import com.project.securitybackend.repository.IUserRepository;
import com.project.securitybackend.security.TokenUtils;
import com.project.securitybackend.service.definition.IAuthService;
import com.project.securitybackend.service.definition.IEmailService;
import com.project.securitybackend.util.enums.UserRole;
import com.project.securitybackend.util.enums.UserStatus;
import com.project.securitybackend.util.exceptions.UserExceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
@Service
public class AuthService implements IAuthService {

    private final IUserRepository _userRepository;
    private final ISimpleUserRepository _simpleUserRepository;
    private final IAuthorityRepository _authorityRepository;
    private final PasswordEncoder _passwordEncoder;
    private final AuthenticationManager _authenticationManager;
    private final TokenUtils _tokenUtils;
    private final IEmailService _emailService;

    public AuthService(IUserRepository userRepository, ISimpleUserRepository simpleUserRepository, IAuthorityRepository authorityRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenUtils tokenUtils, IEmailService emailService) {
        _userRepository = userRepository;
        _simpleUserRepository = simpleUserRepository;
        _authorityRepository = authorityRepository;
        _passwordEncoder = passwordEncoder;
        _authenticationManager = authenticationManager;
        _tokenUtils = tokenUtils;
        _emailService = emailService;
    }

    @Override
    public UserResponse login(LoginRequest request) {
        User user = _userRepository.findOneByUsername(request.getUsername());
        if(!isUserFound(user, request)) {
            throw new UserNotFoundException();
        }

        checkSimpleUserStatus(user);
        checkUserConfirmationStatus(user);
        Authentication authentication = loginSimpleUser(request.getUsername(), request.getPassword());
        return createLoginUserResponse(authentication, user);
    }

    @Override
    public RegistrationResponse registration(RegistrationRequest request) {
        User user = _userRepository.findOneByUsername(request.getUsername());
        if(user != null) {
            throw new UserAlreadyExistException(request.getUsername());
        }

        SimpleUser simpleUser = createSimpleUser(request);
        return mapRegistrationToRegistrationResponse(simpleUser);
    }

    @Override
    public ResponseEntity<?> recoverPassword(RecoverPasswordRequest request) {
        String username = _tokenUtils.getUsernameFromToken(request.getUser());
        User user = _userRepository.findOneByUsername(username);
        if(user == null){
            return new ResponseEntity<>("An error has occurred", HttpStatus.BAD_REQUEST);
        }

        if(!request.getPassword().equals(request.getRepeatedPassword())){
            return new ResponseEntity<>("Your password and repeated password don't match, try again.", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(_passwordEncoder.encode(request.getPassword()));
        _userRepository.save(user);

        return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> forgotPassword(EmailRequestDTO request) throws MessagingException {
        User user = _userRepository.findOneByUsername(request.getEmail());
        if(user == null){
            return new ResponseEntity<>("User not found with the email provided.", HttpStatus.NOT_FOUND);
        }

        return _emailService.sendPasswordRecoveryEmail("Password recovery link", request.getEmail(), null, user.getFirstName());
    }

    private RegistrationResponse mapRegistrationToRegistrationResponse(SimpleUser simpleUser) {
        RegistrationResponse registrationResponse = new RegistrationResponse();
        registrationResponse.setMessage("Registration request is successfully created");
        return registrationResponse;
    }

    private SimpleUser createSimpleUser(RegistrationRequest request) {
        SimpleUser simpleUser = new SimpleUser();
        simpleUser.setUserRole(UserRole.SIMPLE_USER);
        simpleUser.setUserStatus(UserStatus.PENDING);
        simpleUser.setUsername(request.getUsername());
        simpleUser.setFirstName(request.getFirstName());
        simpleUser.setLastName(request.getLastName());
        simpleUser.setPassword(_passwordEncoder.encode(request.getPassword()));
        addAuthoritiesSimpleUser(simpleUser);

        return _simpleUserRepository.save(simpleUser);
    }

    private void addAuthoritiesSimpleUser(SimpleUser user) {
        List<Authority> authorities = new ArrayList<>();
        authorities.add(_authorityRepository.findByName("ROLE_SIMPLE_USER"));
        user.setAuthorities(new HashSet<>(authorities));
    }

    private boolean isUserFound(User user, LoginRequest request) {
        return user != null && _passwordEncoder.matches(request.getPassword(), user.getPassword());
    }

    private void checkSimpleUserStatus(User user) {
        if(user.getUserRole() == UserRole.SIMPLE_USER){
            if( ((SimpleUser)user).getUserStatus().equals(UserStatus.PENDING) ) {
                throw new UserRegistrationNotApprovedException();
            }
            if( ((SimpleUser)user).getUserStatus().equals(UserStatus.DENIED) ) {
                throw new UserRegistrationDeniedException();
            }
        }
    }

    private void checkUserConfirmationStatus(User user) {
        SimpleUser simpleUser = _simpleUserRepository.findById(user.getId());
        if(simpleUser != null && !simpleUser.isUserConfirmAccount()) {
            throw new UserAccountNotConfirmedException();
        }
    }

    private Authentication loginSimpleUser(String mail, String password) {
        Authentication authentication = null;
        try {
            authentication = _authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(mail, password));
        }catch (org.springframework.security.authentication.BadCredentialsException e){
            throw new BadCredentialsException();
        }catch (DisabledException e){
            throw new UserRegistrationNotApprovedException();
        }catch (Exception e) {
            e.printStackTrace();
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        return authentication;
    }

    private UserResponse createLoginUserResponse(Authentication authentication, User user) {
        UserDetailsImpl userLog = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = _tokenUtils.generateToken(userLog.getUsername());
        int expiresIn = _tokenUtils.getExpiredIn();

        UserResponse userResponse = mapUserToUserResponse(user);
        userResponse.setToken(jwt);
        userResponse.setTokenExpiresIn(expiresIn);

        return userResponse;
    }

    private UserResponse mapUserToUserResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());

        userResponse.setUsername(user.getUsername());
        userResponse.setUserRole(user.getUserRole().toString());
        return userResponse;
    }
}
