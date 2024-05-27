package md.ceiti.frontend.service;

import com.vaadin.flow.server.StreamResource;
import lombok.RequiredArgsConstructor;
import md.ceiti.frontend.dto.request.ProfileChangePasswordRequest;
import md.ceiti.frontend.dto.request.ProfileUpdateRequest;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.util.ApiUtils;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.JwtUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final RestTemplate restTemplate;

    public Profile getProfile() {
        try {
            return restTemplate.exchange(
                    ApiUtils.PROFILE_ENDPOINT,
                    HttpMethod.GET,
                    ApiUtils.setHeader(JwtUtils.getJwtTokenFromCookie()),
                    Profile.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    public Profile getProfile(String token) {
        try {
            return restTemplate.exchange(
                    ApiUtils.PROFILE_ENDPOINT,
                    HttpMethod.GET,
                    ApiUtils.setHeader(token),
                    Profile.class
                    ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    public Profile updateProfile(ProfileUpdateRequest profileUpdateRequest) {
        try {
            return restTemplate.exchange(
                    ApiUtils.PROFILE_ENDPOINT,
                    HttpMethod.POST,
                    ApiUtils.setHeader(profileUpdateRequest, JwtUtils.getJwtTokenFromCookie()),
                    Profile.class
                    ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    public void changePassword(ProfileChangePasswordRequest profileChangePasswordRequest) {
        try {
            restTemplate.exchange(
                    ApiUtils.PROFILE_ENDPOINT + "/change-image",
                    HttpMethod.POST,
                    ApiUtils.setHeader(profileChangePasswordRequest, JwtUtils.getJwtTokenFromCookie()),
                    Void.class);
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
        }
    }

    public StreamResource getImage() {
        try {
            return new StreamResource("profile-image", () -> new ByteArrayInputStream(
                    Objects.requireNonNull(restTemplate.exchange(
                            ApiUtils.PROFILE_ENDPOINT + "/image",
                            HttpMethod.GET,
                            ApiUtils.setHeader(JwtUtils.getJwtTokenFromCookie()),
                            byte[].class
                    ).getBody())
            ));
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    public Profile changeImage(FileSystemResource imageResource) {
        try {
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            bodyBuilder.part("image", imageResource, MediaType.APPLICATION_OCTET_STREAM);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(JwtUtils.getJwtTokenFromCookie());

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity =
                    new HttpEntity<>(bodyBuilder.build(), headers);

            return restTemplate.exchange(
                    ApiUtils.PROFILE_ENDPOINT + "/change-image",
                    HttpMethod.POST,
                    requestEntity,
                    Profile.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }
}
