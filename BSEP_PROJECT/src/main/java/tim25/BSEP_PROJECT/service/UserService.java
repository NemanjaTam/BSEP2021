package tim25.BSEP_PROJECT.service;

import tim25.BSEP_PROJECT.model.User;
import tim25.BSEP_PROJECT.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User findOneByUserName(String username) {
        return userRepository.findOneByUsername(username);
    }
}
