package md.ceiti.frontend.service.impl;

import lombok.RequiredArgsConstructor;
import md.ceiti.frontend.dto.CmsInstitutionDto;
import md.ceiti.frontend.exception.FrontendException;
import md.ceiti.frontend.service.CrudService;
import md.ceiti.frontend.util.ApiUtils;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.JwtUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CmsInstitutionService implements CrudService<CmsInstitutionDto> {

    private final RestTemplate restTemplate;

    @Override
    public List<CmsInstitutionDto> findAll() {
        try {
            CmsInstitutionDto[] institutionResponses = restTemplate.exchange(
                    ApiUtils.CMS_INSTITUTIONS_ENDPOINT,
                    HttpMethod.GET,
                    ApiUtils.setHeader(JwtUtils.getJwtTokenFromCookie()),
                    CmsInstitutionDto[].class
            ).getBody();

            if (institutionResponses != null) {
                return Arrays.stream(institutionResponses).toList();
            } else {
                throw new FrontendException("Api non responsive");
            }
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    @Override
    public CmsInstitutionDto insert(CmsInstitutionDto request) {
        try {
            return restTemplate.exchange(
                    ApiUtils.CMS_INSTITUTIONS_ENDPOINT + "?master=" + request.getMaster().getId(),
                    HttpMethod.POST,
                    ApiUtils.setHeader(request, JwtUtils.getJwtTokenFromCookie()),
                    CmsInstitutionDto.class
                    ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    @Override
    public CmsInstitutionDto update(CmsInstitutionDto request) {
        try {
            return restTemplate.exchange(
                    ApiUtils.CMS_INSTITUTIONS_ENDPOINT + "?id=" + request.getId() +
                            "&master=" + request.getMaster().getId(),
                    HttpMethod.PUT,
                    ApiUtils.setHeader(request, JwtUtils.getJwtTokenFromCookie()),
                    CmsInstitutionDto.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    @Override
    public void delete(CmsInstitutionDto request) {
        try {
            restTemplate.exchange(
                    ApiUtils.CMS_INSTITUTIONS_ENDPOINT + "?id=" + request.getId(),
                    HttpMethod.DELETE,
                    ApiUtils.setHeader(request, JwtUtils.getJwtTokenFromCookie()),
                    Void.class
            );
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
        }
    }
}
