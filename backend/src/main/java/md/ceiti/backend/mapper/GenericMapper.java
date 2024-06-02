package md.ceiti.backend.mapper;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.cms.CmsAccountDto;
import md.ceiti.backend.dto.cms.CmsInstitutionDto;
import md.ceiti.backend.dto.cms.SimpleAccountDto;
import md.ceiti.backend.dto.institution.AccountDto;
import md.ceiti.backend.dto.institution.CategoryDto;
import md.ceiti.backend.dto.institution.DocumentDto;
import md.ceiti.backend.dto.institution.InstitutionDto;
import md.ceiti.backend.dto.request.ProfileUpdateRequest;
import md.ceiti.backend.dto.response.Profile;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Category;
import md.ceiti.backend.model.Document;
import md.ceiti.backend.model.Institution;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenericMapper {

    private final ModelMapper mapper;

    public Account toEntity(ProfileUpdateRequest registerRequest) {
        return mapper.map(registerRequest, Account.class);
    }

    public Account toEntity(CmsAccountDto accountDto) {
        return mapper.map(accountDto, Account.class);
    }

    public Account toEntity(SimpleAccountDto accountDto) {
        return mapper.map(accountDto, Account.class);
    }

    public Account toEntity(AccountDto accountDto) {
        return mapper.map(accountDto, Account.class);
    }

    public Category toEntity(CategoryDto categoryDto) {
        return mapper.map(categoryDto, Category.class);
    }

    public Document toEntity(DocumentDto documentDto) {
        return mapper.map(documentDto, Document.class);
    }

    public Profile toResponse(Account account) {
        return mapper.map(account, Profile.class);
    }

    public AccountDto toDtoResponse(Account account) {
        return mapper.map(account, AccountDto.class);
    }

    public CategoryDto toDtoResponse(Category category) {
        return mapper.map(category, CategoryDto.class);
    }

    public DocumentDto toDtoResponse(Document document) {
        return mapper.map(document, DocumentDto.class);
    }

    public InstitutionDto toResponse(Institution institution) {
        return mapper.map(institution, InstitutionDto.class);
    }

    public CmsAccountDto toCmsResponse(Account account) {
        return mapper.map(account, CmsAccountDto.class);
    }

    public CmsInstitutionDto toCmsResponse(Institution institution) {
        return mapper.map(institution, CmsInstitutionDto.class);
    }

    public Institution toEntity(CmsInstitutionDto institutionDto) {
        Institution institution = new Institution();
        institution.setId(institutionDto.getId());
        institution.setMaster(toEntity(institutionDto.getMaster()));
        institution.setName(institutionDto.getName());
        institution.setActive(institutionDto.isActive());
        institution.setEnabled(institutionDto.isEnabled());
        return institution;
    }
}
