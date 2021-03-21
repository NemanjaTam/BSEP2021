package tim25.BSEP_PROJECT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim25.BSEP_PROJECT.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUsername(String username);

    User findOneByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);


}