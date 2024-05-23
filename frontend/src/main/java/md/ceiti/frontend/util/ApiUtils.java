package md.ceiti.frontend.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class ApiUtils {

//    private static final String BASE_ENDPOINT = "http://192.168.100.102:8080/api/v1/";
    private static final String BASE_ENDPOINT = "http://localhost:8080/api/v1/";

    public static final String AUTH_ENDPOINT = BASE_ENDPOINT + "auth";

    public static final String PROFILE_ENDPOINT = BASE_ENDPOINT + "profile";

    public static final String IMAGES_ENDPOINT = BASE_ENDPOINT + "images";

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
