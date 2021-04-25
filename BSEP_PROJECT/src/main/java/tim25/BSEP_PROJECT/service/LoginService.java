package tim25.BSEP_PROJECT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import tim25.BSEP_PROJECT.dto.RegistrationDTO;

import tim25.BSEP_PROJECT.model.User;
import tim25.BSEP_PROJECT.repository.UserRepository;

@Service
public class LoginService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthorityService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User loginUser(String username, String password) {
        if (!validateFields(username, password)) {
            return null;
        }
        //System.out.println(passwordEncoder.encode(password));

        User user = (User) loadUserByUsername(username);

        if (user == null)
            return null;
        else
            return user;
    }


    public Boolean registerUser(RegistrationDTO registrationDTO)  {
        User user = userRepo.save(new User(registrationDTO));
        if(user != null) {
            return true;
        } return false;

    }


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findOneByUsername(username);
        return user;
    }

    private boolean validateFields(String username, String password ) {
        return validateUsername(username) && validatePass(password);
    }

    private boolean validateUsername(String username) {
        return username.equals("") ? false : true ;
    }

    private boolean validatePass(String pass) {
        boolean ret = false;
        boolean numCheck = false;
        boolean upperCheck = false;
        boolean lowerCheck = false;

        if (pass.length() < 8) {
            return ret;
        }else {
            for (Character c : pass.toCharArray()) {
                if (Character.isDigit(c)) {
                    numCheck = true;
                    continue;
                } else if (Character.isLowerCase(c)) {
                    lowerCheck = true;
                    continue;
                } else if (Character.isUpperCase(c)) {
                    upperCheck = true;
                    continue;
                }
            }

            if (!numCheck || !lowerCheck || !upperCheck) {
                ret = false;
            } else {
                ret = true;
            }
        }

        return ret;
    }

}
