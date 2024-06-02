package md.ceiti.backend.facade;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.cms.CmsAccountDto;
import md.ceiti.backend.dto.request.ProfileChangePasswordRequest;
import md.ceiti.backend.dto.request.ProfileUpdateRequest;
import md.ceiti.backend.dto.response.Profile;
import md.ceiti.backend.mapper.GenericMapper;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Image;
import md.ceiti.backend.model.Institution;
import md.ceiti.backend.model.Role;
import md.ceiti.backend.security.AccountDetails;
import md.ceiti.backend.service.ImageService;
import md.ceiti.backend.service.impl.AccountService;
import md.ceiti.backend.service.impl.InstitutionService;
import md.ceiti.backend.validator.AccountValidator;
import md.ceiti.backend.validator.ProfileChangePasswordRequestValidator;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountFacade {

    private final AccountService accountService;
    private final ImageService imageService;
    private final InstitutionService institutionService;
    private final AccountValidator accountValidator;
    private final ProfileChangePasswordRequestValidator profileChangePasswordRequestValidator;
    private final GenericMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

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

    public byte[] getImage(AccountDetails accountDetails) {
        return imageService.getImage(accountDetails.getAccount().getImage());
    }

    public Profile changeImage(AccountDetails accountDetails,
                               MultipartFile imageResource) {
        Account account = accountDetails.getAccount();
        boolean hasImage = account.getImage() != null;
        UUID originalImageUuid = account.getImage() != null ? account.getImage().getId() : UUID.randomUUID();

        Image image = imageService.insert(imageResource);
        account.setImage(image);
        account = accountService.update(account, account);
        if (hasImage) {
            imageService.deleteById(originalImageUuid);
        }

        return mapper.toResponse(account);
    }

    public List<CmsAccountDto> findAll() {
        return accountService.findAll()
                .stream()
                .map(mapper::toCmsResponse)
                .peek(accountDto -> {
                    accountDto.setPassword(null);
                })
                .toList();
    }

    public Long insertGhost(CmsAccountDto accountDto,
                            BindingResult bindingResult) {
        Account account = mapper.toEntity(accountDto);
        account.setRole(Role.GHOST);
        account.setEnabled(true);

        accountValidator.validate(account, bindingResult);

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        return accountService.insert(account).getId();
    }

    public CmsAccountDto insert(Long institutionId,
                                CmsAccountDto accountDto,
                                BindingResult bindingResult) {
        Account account = mapper.toEntity(accountDto);
        if (Role.INSTITUTION_MASTER.equals(account.getRole()) || Role.INSTITUTION_USER.equals(account.getRole())) {
            Institution institution = institutionService.getById(institutionId);
            account.setInstitution(institution);
        }
        accountValidator.validate(account, bindingResult);
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        return mapper.toCmsResponse(
                accountService.insert(account)
        );
    }

    public CmsAccountDto update(Long id,
                                Long institutionId,
                                CmsAccountDto accountDto,
                                BindingResult bindingResult) {
        Account account = accountService.getById(id);
        Account updatedAccount = mapper.toEntity(accountDto);
        if (Role.INSTITUTION_MASTER.equals(updatedAccount.getRole())
                || Role.INSTITUTION_USER.equals(updatedAccount.getRole())) {
            Institution institution = institutionService.getById(institutionId);
            updatedAccount.setInstitution(institution);
        } else {
            updatedAccount.setInstitution(null);
        }

        if (updatedAccount.getPassword() == null || updatedAccount.getPassword().isBlank()) {
            updatedAccount.setPassword("Str0ngPassw0rd!");
            accountValidator.validate(updatedAccount, bindingResult, true);
            updatedAccount.setPassword(account.getPassword());
        } else {
            accountValidator.validate(updatedAccount, bindingResult, true);
            updatedAccount.setPassword(passwordEncoder.encode(updatedAccount.getPassword()));
        }

        return mapper.toCmsResponse(
                accountService.update(account, updatedAccount)
        );
    }

    public void delete(Long id) {
        Account account = accountService.getById(id);
        account.setEnabled(false);
        accountService.update(account, account);
    }
}
