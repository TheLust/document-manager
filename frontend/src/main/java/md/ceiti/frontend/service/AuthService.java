package md.ceiti.frontend.service;

import lombok.RequiredArgsConstructor;
import md.ceiti.frontend.dto.request.LoginRequest;
import md.ceiti.frontend.exception.BadRequestException;
import md.ceiti.frontend.exception.ExceptionResponse;
import md.ceiti.frontend.util.ApiUtils;
import md.ceiti.frontend.util.ErrorHandler;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;

    public String login(LoginRequest loginRequest) {
        try {
            return restTemplate.exchange(
                    ApiUtils.AUTH_ENDPOINT + "/login",
                    HttpMethod.POST,
                    ApiUtils.setHeader(loginRequest),
                    String.class
            ).getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }
}
