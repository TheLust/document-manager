package md.ceiti.backend.validator;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Role;
import md.ceiti.backend.service.impl.AccountService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AccountValidator implements Validator, UpdateValidator {

    private final AccountService accountService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Account.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        validate(target, errors, false);
    }

    @Override
    public void validate(Object target, Errors errors, boolean isUpdate) {
        Account account = (Account) target;
        GenericValidator.validate(account, errors);
        if (!account.getRole().equals(Role.MASTER)) {
            GenericValidator.notNull(errors, "institution", account.getInstitution());
        }

        if (!isUpdate) {
            GenericValidator.unique(errors, "username", accountService.findByUsername(account.getUsername()));
            GenericValidator.unique(errors, "email", accountService.findByEmail(account.getEmail()));
            GenericValidator.unique(errors, "phoneNumber", accountService.findByPhoneNumber(account.getPhoneNumber()));
        } else {
            GenericValidator.unique(errors,
                    "username",
                    String.class,
                    account,
                    accountService.findByUsername(account.getUsername()));
            GenericValidator.unique(errors,
                    "email",
                    String.class,
                    account,
                    accountService.findByEmail(account.getEmail()));
            GenericValidator.unique(errors,
                    "phoneNumber",
                    String.class,
                    account,
                    accountService.findByPhoneNumber(account.getPhoneNumber()));
        }

        GenericValidator.throwValidationException(errors);
    }
}
