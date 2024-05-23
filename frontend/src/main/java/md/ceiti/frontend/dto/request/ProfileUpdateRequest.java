package md.ceiti.frontend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import md.ceiti.frontend.constant.ConstraintViolationMessage;
import md.ceiti.frontend.constant.Constraints;
import md.ceiti.frontend.constant.fields.ProfileUpdateRequestFields;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileUpdateRequest {

    @NotBlank(message = ProfileUpdateRequestFields.USERNAME_LABEL + ConstraintViolationMessage.REQUIRED)
    @Size(
            min = Constraints.USERNAME_MIN,
            max = Constraints.USERNAME_MAX,
            message = ProfileUpdateRequestFields.USERNAME_LABEL + ConstraintViolationMessage.LENGTH
    )
    private String username;

    @Email(message = ProfileUpdateRequestFields.EMAIL_LABEL + ConstraintViolationMessage.EMAIL)
    @NotBlank(message = ProfileUpdateRequestFields.EMAIL_LABEL + ConstraintViolationMessage.REQUIRED)
    private String email;

    @NotBlank(message = ProfileUpdateRequestFields.PHONE_NUMBER_LABEL + ConstraintViolationMessage.REQUIRED)
    private String phoneNumber;

    @NotBlank(message = ProfileUpdateRequestFields.FIRST_NAME_LABEL + ConstraintViolationMessage.REQUIRED)
    private String firstName;

    @NotBlank(message = ProfileUpdateRequestFields.LAST_NAME_LABEL + ConstraintViolationMessage.REQUIRED)
    private String lastName;

    @Past(message = ProfileUpdateRequestFields.BIRTH_DATE_LABEL + ConstraintViolationMessage.PAST)
    @NotNull(message = ProfileUpdateRequestFields.BIRTH_DATE_LABEL + ConstraintViolationMessage.REQUIRED)
    private LocalDate birthDate;
}
