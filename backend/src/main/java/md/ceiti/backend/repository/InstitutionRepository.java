package md.ceiti.backend.repository;

import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    Optional<Institution> findByMaster(Account account);
    List<Institution> findAllByEnabledIsTrue();
}
