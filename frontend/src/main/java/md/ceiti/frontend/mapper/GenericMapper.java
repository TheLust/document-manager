package md.ceiti.frontend.mapper;

import lombok.RequiredArgsConstructor;
import md.ceiti.frontend.dto.request.ProfileUpdateRequest;
import md.ceiti.frontend.dto.response.Profile;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenericMapper {

    private final ModelMapper mapper;

    public ProfileUpdateRequest toUpdateRequest(Profile profile) {
        return mapper.map(profile, ProfileUpdateRequest.class);
    }
}
