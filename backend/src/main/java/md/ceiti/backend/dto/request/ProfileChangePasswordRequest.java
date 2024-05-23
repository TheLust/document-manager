package md.ceiti.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.backend.constant.ConstraintViolationCodes;
import md.ceiti.backend.constant.Constraints;

@Getter
@Setter
public class ProfileChangePasswordRequest {

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    private String currentPassword;

    @NotBlank(message = ConstraintViolationCodes.REQUIRED)
    @Size(
            min = Constraints.PASSWORD_MIN,
            max = Constraints.PASSWORD_MAX,
            message = ConstraintViolationCodes.LENGTH
    )
    private String newPassword;
}
