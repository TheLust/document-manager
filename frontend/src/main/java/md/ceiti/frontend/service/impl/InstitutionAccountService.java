package md.ceiti.frontend.service.impl;

import md.ceiti.frontend.dto.institution.AccountDto;
import md.ceiti.frontend.util.ApiUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class InstitutionAccountService extends GenericCrudService<AccountDto> {

    public InstitutionAccountService(RestTemplate restTemplate, String institutionId) {
        super(restTemplate);
        setType(AccountDto.class);
        setEndpoint(String.format(ApiUtils.INSTITUTION_USERS_ENDPOINT, institutionId));
        setUpdateQueryParams(Map.of("id", "id"));
        setDeleteQueryParams(Map.of("id", "id"));
    }
}
