package md.ceiti.backend.mapper;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.CmsAccountDto;
import md.ceiti.backend.dto.CmsInstitutionDto;
import md.ceiti.backend.dto.request.ProfileUpdateRequest;
import md.ceiti.backend.dto.request.RegisterRequest;
import md.ceiti.backend.dto.response.Profile;
import md.ceiti.backend.model.Account;
import md.ceiti.backend.model.Institution;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenericMapper {

    private final ModelMapper mapper;

    public Account toEntity(RegisterRequest registerRequest) {
        return mapper.map(registerRequest, Account.class);
    }

    public Account toEntity(ProfileUpdateRequest registerRequest) {
        return mapper.map(registerRequest, Account.class);
    }

    public Account toEntity(CmsAccountDto accountDto) {
        return mapper.map(accountDto, Account.class);
    }

    public Profile toResponse(Account account) {
        return mapper.map(account, Profile.class);
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
        return institution;
    }
}
