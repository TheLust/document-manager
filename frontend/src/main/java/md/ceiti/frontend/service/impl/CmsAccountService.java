package md.ceiti.frontend.service.impl;

import md.ceiti.frontend.dto.CmsAccountDto;
import md.ceiti.frontend.util.ApiUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CmsAccountService extends GenericCrudService<CmsAccountDto> {

    public CmsAccountService(RestTemplate restTemplate) {
        super(restTemplate);
        setType(CmsAccountDto.class);
        setEndpoint(ApiUtils.CMS_ACCOUNTS_ENDPOINT);
        setInsertQueryParams(Map.of("institution", "institution.id"));
        setUpdateQueryParams(Map.of(
                "id", "id",
                "institution", "institution.id"
        ));
        setDeleteQueryParams(Map.of("id", "id"));
    }
}
