package md.ceiti.backend.facade;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.CmsInstitutionDto;
import md.ceiti.backend.mapper.GenericMapper;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Institution;
import md.ceiti.backend.model.Role;
import md.ceiti.backend.service.impl.AccountService;
import md.ceiti.backend.service.impl.InstitutionService;
import md.ceiti.backend.validator.InstitutionValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InstitutionFacade {

    private final InstitutionService institutionService;
    private final AccountService accountService;
    private final GenericMapper mapper;
    private final InstitutionValidator institutionValidator;

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
        accountService.updateAccountsToGhostByInstitution(institution);

        institutionService.delete(institution);
    }
}
