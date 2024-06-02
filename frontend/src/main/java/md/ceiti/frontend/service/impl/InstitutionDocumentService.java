package md.ceiti.frontend.service.impl;

import md.ceiti.frontend.dto.institution.DocumentDto;
import md.ceiti.frontend.util.ApiUtils;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.JwtUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class InstitutionDocumentService extends GenericCrudService<DocumentDto> {

    private final RestTemplate restTemplate;
    private final String endpoint;

    public InstitutionDocumentService(RestTemplate restTemplate, String institutionId) {
        super(restTemplate);
        this.restTemplate = restTemplate;
        this.endpoint = String.format(ApiUtils.INSTITUTION_DOCUMENTS_ENDPOINT, institutionId);
        setType(DocumentDto.class);
        setEndpoint(endpoint);
        setInsertQueryParams(Map.of("category", "category.id"));
        setUpdateQueryParams(Map.of(
                "id", "id",
                "category", "category.id"
        ));
        setDeleteQueryParams(Map.of("id", "id"));
    }

    public DocumentDto insert(DocumentDto documentDto, FileSystemResource file) {
        try {
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("document", file, MediaType.APPLICATION_OCTET_STREAM);
            bodyBuilder.part("dto", documentDto, MediaType.APPLICATION_JSON);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(JwtUtils.getJwtTokenFromCookie());

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity =
                    new HttpEntity<>(bodyBuilder.build(), headers);

            return restTemplate.exchange(
                    endpoint + getQueryParams(documentDto, getInsertQueryParams()),
                    HttpMethod.POST,
                    requestEntity,
                    DocumentDto.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }
}
