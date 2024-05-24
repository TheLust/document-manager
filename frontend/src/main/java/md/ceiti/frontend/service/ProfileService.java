package md.ceiti.frontend.service;

import com.vaadin.flow.server.StreamResource;
import lombok.RequiredArgsConstructor;
import md.ceiti.frontend.dto.request.ProfileChangePasswordRequest;
import md.ceiti.frontend.dto.request.ProfileUpdateRequest;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.util.ApiUtils;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.JwtUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
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
                            Profile.class)
                    .getBody();
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
                            Profile.class)
                    .getBody();
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
                            Profile.class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    public void changePassword(ProfileChangePasswordRequest profileChangePasswordRequest) {
        try {
            restTemplate.exchange(ApiUtils.PROFILE_ENDPOINT + "/change-image",
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
                                    byte[].class)
                            .getBody())
            ));
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }

    public Profile changeImage() {
        return new Profile();
    }
}
