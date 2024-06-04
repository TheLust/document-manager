package md.ceiti.backend.facade;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.constant.ErrorCodes;
import md.ceiti.backend.dto.cms.CmsInstitutionDto;
import md.ceiti.backend.dto.institution.AccountDto;
import md.ceiti.backend.dto.institution.CategoryDto;
import md.ceiti.backend.dto.institution.DocumentDto;
import md.ceiti.backend.dto.institution.InstitutionDto;
import md.ceiti.backend.exception.AccessDeniedException;
import md.ceiti.backend.exception.ApplicationException;
import md.ceiti.backend.mapper.GenericMapper;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Category;
import md.ceiti.backend.model.Document;
import md.ceiti.backend.model.Institution;
import md.ceiti.backend.model.Role;
import md.ceiti.backend.service.impl.AccountService;
import md.ceiti.backend.service.impl.CategoryService;
import md.ceiti.backend.service.impl.DocumentService;
import md.ceiti.backend.service.impl.InstitutionService;
import md.ceiti.backend.validator.AccountValidator;
import md.ceiti.backend.validator.CategoryValidator;
import md.ceiti.backend.validator.GenericValidator;
import md.ceiti.backend.validator.InstitutionValidator;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InstitutionFacade {

    private final InstitutionService institutionService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final DocumentService documentService;
    private final GenericMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final InstitutionValidator institutionValidator;
    private final AccountValidator accountValidator;
    private final CategoryValidator categoryValidator;

    public List<CmsInstitutionDto> findAll() {
        return institutionService.findAll()
                .stream()
                .map(mapper::toCmsResponse)
                .toList();
    }

    public CmsInstitutionDto insert(Long masterId,
                                    CmsInstitutionDto institutionDto,
                                    BindingResult bindingResult) {
        Account account = accountService.getById(masterId);
        Institution institution = mapper.toEntity(institutionDto);
        institution.setMaster(account);
        institutionValidator.validate(institution, bindingResult);
        institution = institutionService.insert(institution);

        if (!Role.MASTER.equals(account.getRole())) {
            account.setRole(Role.INSTITUTION_MASTER);
            account.setInstitution(institution);
        }

        accountService.update(account, account);
        return mapper.toCmsResponse(institution);
    }

    public CmsInstitutionDto update(Long id,
                                    Long masterId,
                                    CmsInstitutionDto institutionDto,
                                    BindingResult bindingResult) {
        Institution institution = institutionService.getById(id);
        Account account = accountService.getById(masterId);
        Institution updatedInstitution = mapper.toEntity(institutionDto);
        updatedInstitution.setMaster(account);
        institutionValidator.validate(updatedInstitution, bindingResult, true);
        updatedInstitution = institutionService.update(institution, updatedInstitution);

        if (!Role.MASTER.equals(account.getRole())) {
            account.setRole(Role.INSTITUTION_MASTER);
            account.setInstitution(updatedInstitution);
        }

        accountService.update(account, account);
        return mapper.toCmsResponse(updatedInstitution);
    }

    public void delete(Long id) {
        Institution institution = institutionService.getById(id);
        accountService.updateAccountsToDisabledByInstitution(institution);

        institution.setEnabled(false);
        institutionService.update(institution, institution);
    }

    public InstitutionDto get(Long institutionId,
                              Account account) {
        checkAccess(institutionId, account);
        return mapper.toResponse(
                institutionService.getById(institutionId)
        );
    }

    public List<AccountDto> findAllAccounts(Long institutionId,
                                            Account account) {
        checkMasterAccess(institutionId, account, false);

        Institution institution = institutionService.getById(institutionId);
        return institution.getAccounts()
                .stream()
                .map(mapper::toDtoResponse)
                .filter(accountDto -> !accountDto.getId().equals(account.getId())
                        && accountDto.isEnabled())
                .peek(accountDto -> accountDto.setPassword(null))
                .toList();
    }

    public AccountDto insertAccount(Long institutionId,
                                    Account authAccount,
                                    AccountDto accountDto,
                                    BindingResult bindingResult) {
        checkMasterAccess(institutionId, authAccount);

        Institution institution = institutionService.getById(institutionId);
        Account account = mapper.toEntity(accountDto);
        account.setInstitution(institution);
        account.setRole(Role.INSTITUTION_USER);
        account.setEnabled(true);

        accountValidator.validate(account, bindingResult);

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return mapper.toDtoResponse(
                accountService.insert(account)
        );
    }

    public AccountDto updateAccount(Long institutionId,
                                    Account authAccount,
                                    Long accountId,
                                    AccountDto accountDto,
                                    BindingResult bindingResult) {
        checkMasterAccess(institutionId, authAccount);

        Account account = accountService.getById(accountId);
        Account updatedAccount = mapper.toEntity(accountDto);
        checkHierarchy(authAccount, account);

        updatedAccount.setInstitution(account.getInstitution());
        updatedAccount.setRole(account.getRole());

        if (updatedAccount.getPassword() == null || updatedAccount.getPassword().isBlank()) {
            updatedAccount.setPassword("Str0ngPassw0rd!");
            accountValidator.validate(updatedAccount, bindingResult, true);
            updatedAccount.setPassword(account.getPassword());
        } else {
            accountValidator.validate(updatedAccount, bindingResult, true);
            updatedAccount.setPassword(passwordEncoder.encode(updatedAccount.getPassword()));
        }

        return mapper.toDtoResponse(
                accountService.update(account, updatedAccount)
        );
    }

    public void deleteAccount(Long institutionId,
                              Account authAccount,
                              Long accountId) {
        checkMasterAccess(institutionId, authAccount);
        Account account = accountService.getById(accountId);

        checkHierarchy(authAccount, account);

        account.setEnabled(false);
        account.setActive(false);
        accountService.update(account, account);
    }

    public List<CategoryDto> findAllCategories(Long institutionId,
                                               Account account) {
        checkAccess(institutionId, account);
        Institution institution = institutionService.getById(institutionId);
        return institution.getCategories()
                .stream()
                .map(mapper::toDtoResponse)
                .toList();
    }

    public CategoryDto insertCategory(Long institutionId,
                                      Account authAccount,
                                      CategoryDto categoryDto,
                                      BindingResult bindingResult) {
        checkMasterAccess(institutionId, authAccount);

        Institution institution = institutionService.getById(institutionId);
        Category category = mapper.toEntity(categoryDto);
        category.setInstitution(institution);

        categoryValidator.validate(category, bindingResult);
        return mapper.toDtoResponse(
                categoryService.insert(category)
        );
    }

    public CategoryDto updateCategory(Long institutionId,
                                      Account authAccount,
                                      Long categoryId,
                                      CategoryDto categoryDto,
                                      BindingResult bindingResult) {
        checkMasterAccess(institutionId, authAccount);

        Category category = categoryService.getById(categoryId);
        Category updatedCategory = mapper.toEntity(categoryDto);
        updatedCategory.setInstitution(category.getInstitution());

        categoryValidator.validate(updatedCategory, bindingResult, true);
        return mapper.toDtoResponse(
                categoryService.update(category, updatedCategory)
        );
    }

    public void deleteCategory(Long institutionId,
                               Account authAccount,
                               Long categoryId) {
        checkMasterAccess(institutionId, authAccount);
        Category category = categoryService.getById(categoryId);
        category.setEnabled(false);
        categoryService.update(category, category);
    }

    public List<DocumentDto> findAllDocuments(Long institutionId,
                                              Account account) {
        checkAccess(institutionId, account);
        Institution institution = institutionService.getById(institutionId);
        return institution.getCategories()
                .stream()
                .filter(Category::isEnabled)
                .flatMap(category -> category.getDocuments()
                        .stream()
                        .map(mapper::toDtoResponse))
                .toList();
    }

    public DocumentDto insertDocument(Long institutionId,
                                      Account account,
                                      Long categoryId,
                                      MultipartFile multipartFile,
                                      DocumentDto documentDto,
                                      BindingResult bindingResult) {
        checkAccess(institutionId, account);

        Category category = categoryService.getById(categoryId);
        if (!institutionId.equals(category.getInstitution().getId())) {
            throw new ApplicationException("Category is not from current institution", ErrorCodes.BAD_REQUEST);
        }
        Document document = mapper.toEntity(documentDto);
        document.setCategory(category);
        if (document.getName() != null && document.getName().isBlank()) {
            document.setName(FilenameUtils.getName(multipartFile.getOriginalFilename()));
        }
        document.setExtension(FilenameUtils.getExtension(multipartFile.getOriginalFilename()));
        document.setCreatedBy(account);
        document.setCreatedAt(LocalDate.now());

        GenericValidator.validate(document, bindingResult);
        GenericValidator.throwValidationException(bindingResult);

        document = documentService.insert(document);

        try {
            saveDocument(document, multipartFile);
        } catch (IOException e) {
            documentService.delete(document);
            throw new ApplicationException("Could not save document", ErrorCodes.INTERNAL_ERROR);
        }

        return mapper.toDtoResponse(document);
    }

    public DocumentDto updateDocument(Long institutionId,
                                      Account account,
                                      UUID documentId,
                                      Long categoryId,
                                      DocumentDto documentDto,
                                      BindingResult bindingResult) {
        checkAccess(institutionId, account);

        Document document = documentService.getById(documentId);
        if (!List.of(Role.INSTITUTION_MASTER, Role.MASTER).contains(account.getRole())
                && !document.getCreatedBy().getId().equals(account.getId())) {
            throw new ApplicationException("You cannot update the document of another person", ErrorCodes.ACCESS_DENIED);
        }

        Document updatedDocument = mapper.toEntity(documentDto);
        updatedDocument.setCreatedAt(document.getCreatedAt());
        updatedDocument.setCreatedBy(document.getCreatedBy());
        updatedDocument.setExtension(document.getExtension());

        if (categoryId.equals(document.getCategory().getId())) {
            updatedDocument.setCategory(document.getCategory());
        } else {
            Category category = categoryService.getById(categoryId);
            updatedDocument.setCategory(category);
        }

        GenericValidator.validate(updatedDocument, bindingResult);
        GenericValidator.throwValidationException(bindingResult);

        return mapper.toDtoResponse(
                documentService.update(document, updatedDocument)
        );
    }

    public void deleteDocument(Long institutionId,
                               Account account,
                               UUID documentId) {
        checkAccess(institutionId, account);

        Document document = documentService.getById(documentId);
        if (!Role.INSTITUTION_MASTER.equals(account.getRole())
                && !document.getCreatedBy().getId().equals(account.getId())) {
            throw new ApplicationException("You cannot update the document of another person", ErrorCodes.ACCESS_DENIED);
        }

        deleteDocument(document);
        documentService.delete(document);
    }

    public byte[] downloadDocument(Long institutionId,
                                   Account account,
                                   UUID documentId) {
        checkAccess(institutionId, account);

        Document document = documentService.getById(documentId);
        return getDocument(document);
    }

    private byte[] getDocument(Document document) {
        String DOCUMENT_PATH = "files/documents/%s.%s";
        File file = new File(String.format(DOCUMENT_PATH, document.getId(), document.getExtension()));
        try (InputStream inputStream = new FileInputStream(file)) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new ApplicationException("Could not get document from local storage", ErrorCodes.INTERNAL_ERROR);
        }
    }

    private void saveDocument(Document document, MultipartFile multipartFile) throws IOException {
        String DOCUMENT_PATH = "files/documents/%s.%s";
        File file = new File(String.format(DOCUMENT_PATH, document.getId(), document.getExtension()));
        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        }
    }

    private void deleteDocument(Document document) {
        String DOCUMENT_PATH = "files/documents/%s.%s";
        File file = new File(String.format(DOCUMENT_PATH, document.getId(), document.getExtension()));
        if (!file.delete()) {
            throw new ApplicationException("Could not delete document", ErrorCodes.INTERNAL_ERROR);
        }
    }

    private void checkAccess(Long institutionId,
                             Account account) {
        if (Role.MASTER.equals(account.getRole())) {
            return;
        }

        if (Role.GHOST.equals(account.getRole())) {
            throw new AccessDeniedException("Cannot access any resources as a GHOST");
        }

        if (!account.getInstitution().getId().equals(institutionId)) {
            throw new AccessDeniedException("Cannot access information of this institution");
        }
    }

    private void checkMasterAccess(Long institutionId,
                                   Account account) {
        checkMasterAccess(institutionId, account, true);
    }

    private void checkMasterAccess(Long institutionId,
                                   Account account,
                                   boolean checkActive) {
        checkAccess(institutionId, account);

        if (!List.of(Role.INSTITUTION_MASTER, Role.MASTER).contains(account.getRole())) {
            throw new AccessDeniedException("Cannot access this resource w/o having INSTITUTION_MASTER role");
        }

        if (checkActive) {
            if (!account.isActive()) {
                throw new AccessDeniedException("Account is not active!");
            }
        }
    }

    private void checkHierarchy(Account account,
                                Account target) {
        if (!List.of(Role.MASTER, Role.INSTITUTION_MASTER).contains(account.getRole())) {
            throw new AccessDeniedException("Insufficient authority to access this resource");
        }

        if (!Role.INSTITUTION_USER.equals(target.getRole())) {
            throw new AccessDeniedException("Target user must be a simple user");
        }

        Institution institution = account.getInstitution();
        Institution targetInstitution = target.getInstitution();

        if (institution == null || targetInstitution == null) {
            throw new AccessDeniedException("Cannot perform this action is one of the parties does not have a institution");
        }

        if (!institution.getId().equals(targetInstitution.getId())) {
            throw new AccessDeniedException("Cannot target user from another institution");
        }
    }
}
