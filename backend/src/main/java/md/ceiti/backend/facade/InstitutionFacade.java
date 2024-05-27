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
        account.setRole(Role.INSTITUTION_MASTER);
        account = accountService.update(account, account);

        Institution institution = mapper.toEntity(institutionDto);
        institution.setMaster(account);

        institutionValidator.validate(institution, bindingResult);

        return mapper.toCmsResponse(
                institutionService.insert(institution)
        );
    }

    public CmsInstitutionDto update(CmsInstitutionDto institutionRequest) {
        return new CmsInstitutionDto();
    }
}
