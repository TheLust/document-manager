package md.ceiti.frontend.service;

import lombok.RequiredArgsConstructor;
import md.ceiti.frontend.dto.request.ProfileChangePasswordRequest;
import md.ceiti.frontend.dto.request.ProfileUpdateRequest;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.exception.ExceptionResponse;
import md.ceiti.frontend.util.ApiUtils;
import md.ceiti.frontend.util.JwtUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
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
            throw new BadRequestException(Objects.requireNonNull(e.getResponseBodyAs(ExceptionResponse.class)).getErrorCode());
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
            throw new BadRequestException(Objects.requireNonNull(e.getResponseBodyAs(ExceptionResponse.class)).getErrorCode());
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
            ApiUtils.handleApiBadRequest(e);
            throw new BadRequestException(Objects.requireNonNull(e.getResponseBodyAs(ExceptionResponse.class)).getErrorCode());
        }
    }

    public void changePassword(ProfileChangePasswordRequest profileChangePasswordRequest) {
        try {
            restTemplate.exchange(ApiUtils.PROFILE_ENDPOINT + "/change-password",
                            HttpMethod.POST,
                            ApiUtils.setHeader(profileChangePasswordRequest, JwtUtils.getJwtTokenFromCookie()),
                            Void.class);
        } catch (HttpClientErrorException e) {
            ApiUtils.handleApiBadRequest(e);
            throw new BadRequestException(Objects.requireNonNull(e.getResponseBodyAs(ExceptionResponse.class)).getErrorCode());
        }
    }
}
