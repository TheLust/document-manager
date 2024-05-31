package md.ceiti.backend.repository;

import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByPhoneNumber(String phoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE Account a set a.institution = null, a.role = 'GHOST' where a.institution = :institution")
    void updateAccountsToGhostByInstitution(Institution institution);
}
