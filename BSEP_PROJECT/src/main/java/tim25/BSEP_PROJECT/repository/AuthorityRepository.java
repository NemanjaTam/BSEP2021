package tim25.BSEP_PROJECT.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tim25.BSEP_PROJECT.model.Authority;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long>{
    public Authority findOneByName(String name);
}
