package md.ceiti.frontend.service.impl;

import lombok.RequiredArgsConstructor;
import md.ceiti.frontend.dto.institution.InstitutionDto;
import md.ceiti.frontend.util.ApiUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final RestTemplate restTemplate;

    public InstitutionDto get(String institutionId) {
        return ApiUtils.get(restTemplate, ApiUtils.getInstitutionsEndpoint(institutionId), InstitutionDto.class);
    }
}
