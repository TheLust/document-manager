package md.ceiti.backend.mapper;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.dto.request.ProfileUpdateRequest;
import md.ceiti.backend.dto.request.RegisterRequest;
import md.ceiti.backend.dto.response.Profile;
import md.ceiti.backend.model.Account;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenericMapper {

    private final ModelMapper mapper;

    public Account toEntity(RegisterRequest registerRequest) {
        return mapper.map(registerRequest, Account.class);
    }

    public Account toEntity(ProfileUpdateRequest registerRequest) {
        return mapper.map(registerRequest, Account.class);
    }

    public Profile toResponse(Account account) {
        return mapper.map(account, Profile.class);
    }
}