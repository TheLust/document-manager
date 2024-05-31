package md.ceiti.frontend.service.impl;

import md.ceiti.frontend.dto.CmsInstitutionDto;
import md.ceiti.frontend.util.ApiUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CmsInstitutionService extends GenericCrudService<CmsInstitutionDto> {

    public CmsInstitutionService(RestTemplate restTemplate) {
        super(restTemplate);
        setType(CmsInstitutionDto.class);
        setEndpoint(ApiUtils.CMS_INSTITUTIONS_ENDPOINT);
        setInsertQueryParams(Map.of("master", "master.id"));
        setUpdateQueryParams(Map.of(
                "id", "id",
                "master", "master.id"
        ));
        setDeleteQueryParams(Map.of("id", "id"));
    }
}
