package md.ceiti.backend.service.impl;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.exception.NotFoundException;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Institution;
import md.ceiti.backend.repository.AccountRepository;
import md.ceiti.backend.service.GenericService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService implements GenericService<Account, Long> {

    private final AccountRepository accountRepository;

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public Account getById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account", "id", id));
    }

    @Override
    public Account insert(Account entity) {
        return accountRepository.save(entity);
    }

    @Override
    public Account update(Account presentEntity, Account updatedEntity) {
        BeanUtils.copyProperties(updatedEntity, presentEntity,
                "id");
        return accountRepository.save(presentEntity);
    }

    @Override
    public void delete(Account entity) {
        accountRepository.delete(entity);
    }

    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findByPhoneNumber(String phoneNumber) {
        return accountRepository.findByPhoneNumber(phoneNumber);
    }

    public void updateAccountsToDisabledByInstitution(Institution institution) {
        accountRepository.updateAccountsToDisabledByInstitution(institution);
    }
}
