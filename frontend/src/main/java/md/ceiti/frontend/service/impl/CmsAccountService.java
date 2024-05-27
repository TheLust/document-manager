package md.ceiti.frontend.service.impl;

import lombok.RequiredArgsConstructor;
import md.ceiti.frontend.dto.CmsAccountDto;
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
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CmsAccountService implements CrudService<CmsAccountDto> {

    private final RestTemplate restTemplate;

    @Override
    public List<CmsAccountDto> findAll() {
        try {
            CmsAccountDto[] profiles = restTemplate.exchange(
                    ApiUtils.CMS_ACCOUNTS_ENDPOINT,
                    HttpMethod.GET,
                    ApiUtils.setHeader(JwtUtils.getJwtTokenFromCookie()),
                    CmsAccountDto[].class
            ).getBody();

            if (profiles != null) {
                return Arrays.stream(profiles).toList();
            } else {
                throw new FrontendException("Returned array as null, please check api");
            }
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return Collections.emptyList();
        }
    }

    @Override
    public CmsAccountDto insert(CmsAccountDto request) {
        try {
            return restTemplate.exchange(
                    ApiUtils.CMS_INSTITUTIONS_ENDPOINT,
                    HttpMethod.POST,
                    ApiUtils.setHeader(request, JwtUtils.getJwtTokenFromCookie()),
                    CmsAccountDto.class
                    ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    @Override
    public CmsAccountDto update(CmsAccountDto request) {
        return null;
    }

    @Override
    public void delete(CmsAccountDto request) {

    }
}
