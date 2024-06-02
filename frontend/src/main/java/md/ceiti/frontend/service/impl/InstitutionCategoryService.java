package md.ceiti.frontend.service.impl;

import md.ceiti.frontend.dto.institution.CategoryDto;
import md.ceiti.frontend.util.ApiUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class InstitutionCategoryService extends GenericCrudService<CategoryDto> {

    public InstitutionCategoryService(RestTemplate restTemplate, String institutionId) {
        super(restTemplate);
        setType(CategoryDto.class);
        setEndpoint(String.format(ApiUtils.INSTITUTION_CATEGORIES_ENDPOINT, institutionId));
        setUpdateQueryParams(Map.of("id", "id"));
        setDeleteQueryParams(Map.of("id", "id"));
    }
}
