package md.ceiti.frontend.service;

import lombok.RequiredArgsConstructor;
import md.ceiti.frontend.dto.response.Profile;
import md.ceiti.frontend.util.ApiUtils;
import md.ceiti.frontend.util.ErrorHandler;
import md.ceiti.frontend.util.JwtUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final RestTemplate restTemplate;

    public byte[] getImage(UUID uuid) {
        try {
            return restTemplate.exchange(
                            ApiUtils.IMAGES_ENDPOINT + "/" + uuid,
                            HttpMethod.GET,
                            ApiUtils.setHeader(JwtUtils.getJwtTokenFromCookie()),
                            byte[].class)
                    .getBody();
        } catch (HttpClientErrorException e) {
            ErrorHandler.handle(e);
            return null;
        }
    }
}
