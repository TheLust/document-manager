package md.ceiti.backend.validator;

import lombok.RequiredArgsConstructor;
import md.ceiti.backend.constant.ConstraintViolationCodes;
import md.ceiti.backend.dto.request.ProfileChangePasswordRequest;
import md.ceiti.backend.model.Account;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ProfileChangePasswordRequestValidator implements Validator{

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return ProfileChangePasswordRequest.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }

    public void validate(Object target, Errors errors, Account account) {
        ProfileChangePasswordRequest profileChangePasswordRequest = (ProfileChangePasswordRequest) target;
        GenericValidator.validate(profileChangePasswordRequest, errors);

        if (profileChangePasswordRequest.getCurrentPassword() != null
                && profileChangePasswordRequest.getCurrentPassword().
                equals(profileChangePasswordRequest.getNewPassword())) {
            errors.rejectValue("newPassword", "", ConstraintViolationCodes.SAME_PASSWORD);
        }

        if (!passwordEncoder.matches(profileChangePasswordRequest.getCurrentPassword(), account.getPassword())) {
            errors.rejectValue("currentPassword", "", ConstraintViolationCodes.INCORRECT_PASSWORD);
        }

        GenericValidator.throwValidationException(errors);
    }
}
