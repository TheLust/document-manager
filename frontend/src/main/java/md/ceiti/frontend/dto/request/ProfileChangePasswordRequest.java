package md.ceiti.frontend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.constant.ConstraintViolationMessage;
import md.ceiti.frontend.constant.Constraints;
import md.ceiti.frontend.constant.I18n;
import md.ceiti.frontend.constant.fields.ProfileChangePasswordRequestFields;

@Getter
@Setter
public class ProfileChangePasswordRequest {

    @NotBlank(message = ProfileChangePasswordRequestFields.CURRENT_PASSWORD_LABEL + ConstraintViolationMessage.REQUIRED)
    private String currentPassword;

    @NotBlank(message = ProfileChangePasswordRequestFields.NEW_PASSWORD_LABEL + ConstraintViolationMessage.REQUIRED)
    @Size(
            min = Constraints.PASSWORD_MIN,
            max = Constraints.PASSWORD_MAX,
            message = ProfileChangePasswordRequestFields.NEW_PASSWORD_LABEL + ConstraintViolationMessage.LENGTH
    )
    private String newPassword;

    @NotBlank(message = ProfileChangePasswordRequestFields.CONFIRM_PASSWORD_LABEL + ConstraintViolationMessage.REQUIRED)
    @Size(
            min = Constraints.PASSWORD_MIN,
            max = Constraints.PASSWORD_MAX,
            message = ProfileChangePasswordRequestFields.CONFIRM_PASSWORD_LABEL + ConstraintViolationMessage.LENGTH
    )
    private String confirmPassword;
}
