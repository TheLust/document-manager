package md.ceiti.frontend.util;

import md.ceiti.frontend.exception.FrontendException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ApiUtils {

    private static final String BASE_ENDPOINT = "http://localhost:8080/api/v1/";

    public static final String AUTH_ENDPOINT = BASE_ENDPOINT + "auth";

    public static final String PROFILE_ENDPOINT = BASE_ENDPOINT + "profile";

    public static final String CMS_ENDPOINT = BASE_ENDPOINT + "cms";

    public static final String CMS_INSTITUTIONS_ENDPOINT = CMS_ENDPOINT + "/institutions";

    public static final String CMS_ACCOUNTS_ENDPOINT = CMS_ENDPOINT + "/accounts";

    public static final String INSTITUTIONS_ENDPOINT = BASE_ENDPOINT + "institutions/%s";
    public static final String INSTITUTION_USERS_ENDPOINT = INSTITUTIONS_ENDPOINT + "/users";
    public static final String INSTITUTION_CATEGORIES_ENDPOINT = INSTITUTIONS_ENDPOINT + "/categories";
    public static final String INSTITUTION_DOCUMENTS_ENDPOINT = INSTITUTIONS_ENDPOINT + "/documents";

    public static String getInstitutionsEndpoint(String institutionId) {
        return String.format(INSTITUTIONS_ENDPOINT, institutionId);
    }

    public static <T> T get(RestTemplate restTemplate, String url, Class<T> type) {
        try {
            return  restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    ApiUtils.setHeader(JwtUtils.getJwtTokenFromCookie()),
                    type
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    public static HttpEntity<String> setHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(headers);
    }

    public static HttpEntity<Object> setHeader(Object object) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(object, headers);
    }

    public static HttpEntity<Object> setHeader(Object object, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);
        return new HttpEntity<>(object, headers);
    }
}
