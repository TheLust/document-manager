package md.ceiti.backend.facade;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.request.ProfileChangePasswordRequest;
import md.ceiti.backend.dto.request.ProfileUpdateRequest;
import md.ceiti.backend.dto.request.RegisterRequest;
import md.ceiti.backend.dto.response.Profile;
import md.ceiti.backend.mapper.GenericMapper;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Role;
import md.ceiti.backend.security.AccountDetails;
import md.ceiti.backend.service.impl.AccountService;
import md.ceiti.backend.service.impl.ImageService;
import md.ceiti.backend.service.impl.InstitutionService;
import md.ceiti.backend.validator.AccountValidator;
import md.ceiti.backend.validator.ProfileChangePasswordRequestValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

@Component
@RequiredArgsConstructor
public class AccountFacade {

    private final AccountService accountService;
    private final InstitutionService institutionService;
    private final AccountValidator accountValidator;
    private final ProfileChangePasswordRequestValidator profileChangePasswordRequestValidator;
    private final GenericMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public Profile getProfile(AccountDetails accountDetails) {
        return mapper.toResponse(accountDetails.getAccount());
    }

    public Profile updateProfile(AccountDetails accountDetails,
                                 ProfileUpdateRequest profileUpdateRequest,
                                 BindingResult bindingResult) {

        Account account = accountDetails.getAccount();
        Account updatedEntity = mapper.toEntity(profileUpdateRequest);
        updatedEntity.setRole(account.getRole());
        updatedEntity.setPassword(account.getPassword());
        updatedEntity.setInstitution(account.getInstitution());
        updatedEntity.setImage(account.getImage());
        updatedEntity.setEnabled(account.isEnabled());

        accountValidator.validate(updatedEntity, bindingResult, true);

        return mapper.toResponse(
                accountService.update(account, updatedEntity)
        );
    }

    public void changePassword(AccountDetails accountDetails,
                               ProfileChangePasswordRequest profileChangePasswordRequest,
                               BindingResult bindingResult) {
        Account account = accountDetails.getAccount();
        profileChangePasswordRequestValidator.validate(profileChangePasswordRequest, bindingResult, account);
        account.setPassword(passwordEncoder.encode(profileChangePasswordRequest.getNewPassword()));

        accountService.update(account, account);
    }

    public Account insert(Long institutionId,
                          RegisterRequest registerRequest,
                          BindingResult bindingResult) {
        Account account = mapper.toEntity(registerRequest);
        account.setEnabled(true);

        if (!account.getRole().equals(Role.MASTER)) {
            account.setInstitution(institutionService.getById(institutionId));
        }

        accountValidator.validate(account, bindingResult);

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        return accountService.insert(account);
    }
}
